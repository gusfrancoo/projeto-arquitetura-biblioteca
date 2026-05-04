package biblioteca.aplicacao;

import biblioteca.aplicacao.porta.saida.EmprestimoRepositorioPort;
import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class EmprestimoServico {
    private static final int PRAZO_PADRAO_DIAS = 7;

    private final UsuarioRepositorioPort usuarioRepositorio;
    private final LivroRepositorioPort livroRepositorio;
    private final EmprestimoRepositorioPort emprestimoRepositorio;
    private final AtomicLong geradorId = new AtomicLong(1);

    public EmprestimoServico(
            UsuarioRepositorioPort usuarioRepositorio,
            LivroRepositorioPort livroRepositorio,
            EmprestimoRepositorioPort emprestimoRepositorio
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.livroRepositorio = livroRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
    }

    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + usuarioId));
        Livro livro = livroRepositorio.buscarPorId(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro nao encontrado: " + livroId));

        if (SituacaoUsuario.SUSPENSO.equals(usuario.getSituacao())) {
            throw new IllegalStateException("Usuario suspenso nao pode realizar emprestimos.");
        }

        livro.realizarEmprestimo();

        LocalDate dataRetirada = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(
                geradorId.getAndIncrement(),
                livro,
                usuario,
                dataRetirada,
                dataRetirada.plusDays(PRAZO_PADRAO_DIAS),
                SituacaoEmprestimo.ATIVO
        );

        livroRepositorio.salvar(livro);
        emprestimoRepositorio.salvar(emprestimo);
        return emprestimo;
    }

    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepositorio.buscarPorId(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Emprestimo nao encontrado: " + emprestimoId));

        emprestimo.registrarDevolucao();
        livroRepositorio.salvar(emprestimo.getLivro());
        emprestimoRepositorio.salvar(emprestimo);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepositorio.listarTodos()
                .stream()
                .filter(Emprestimo::estaAtivo)
                .toList();
    }

    public List<Emprestimo> verificarAtrasos() {
        LocalDate hoje = LocalDate.now();
        List<Emprestimo> atrasados = emprestimoRepositorio.listarTodos()
                .stream()
                .filter(emprestimo -> emprestimo.estaAtrasado(hoje))
                .toList();

        atrasados.forEach(emprestimo -> {
            emprestimo.marcarComoAtrasado();
            emprestimoRepositorio.salvar(emprestimo);
        });

        return atrasados;
    }
}
