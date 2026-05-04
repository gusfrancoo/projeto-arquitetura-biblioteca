package biblioteca.apresentacao;

import biblioteca.aplicacao.EmprestimoServico;
import biblioteca.aplicacao.LivroServico;
import biblioteca.aplicacao.UsuarioServico;
import biblioteca.aplicacao.porta.saida.EmprestimoRepositorioPort;
import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.aplicacao.porta.saida.PortaNotificacao;
import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.EmprestimoRepositorioCsv;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.LivroRepositorioCsv;
import biblioteca.infraestrutura.NotificacaoConsole;
import biblioteca.infraestrutura.UsuarioRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorioCsv;

public class Main {
    public static void main(String[] args) {
        PortaNotificacao notificacao = new NotificacaoConsole();

        LivroRepositorioPort livroRepositorioMemoria = new LivroRepositorio();
        UsuarioRepositorioPort usuarioRepositorioMemoria = new UsuarioRepositorio();
        EmprestimoRepositorioPort emprestimoRepositorioMemoria = new EmprestimoRepositorio();

        executarFluxo(
                "Memoria",
                livroRepositorioMemoria,
                usuarioRepositorioMemoria,
                emprestimoRepositorioMemoria,
                notificacao,
                1L,
                1L
        );

        LivroRepositorioPort livroRepositorioCsv = new LivroRepositorioCsv("livros.csv");
        UsuarioRepositorioPort usuarioRepositorioCsv = new UsuarioRepositorioCsv("usuarios.csv");
        EmprestimoRepositorioPort emprestimoRepositorioCsv = new EmprestimoRepositorioCsv(
                "emprestimos.csv",
                livroRepositorioCsv,
                usuarioRepositorioCsv
        );

        executarFluxo(
                "CSV",
                livroRepositorioCsv,
                usuarioRepositorioCsv,
                emprestimoRepositorioCsv,
                notificacao,
                2L,
                2L
        );
    }

    private static void executarFluxo(
            String adaptador,
            LivroRepositorioPort livroRepositorio,
            UsuarioRepositorioPort usuarioRepositorio,
            EmprestimoRepositorioPort emprestimoRepositorio,
            PortaNotificacao notificacao,
            Long livroId,
            Long usuarioId
    ) {
        LivroServico livroServico = new LivroServico(livroRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio);
        EmprestimoServico emprestimoServico = new EmprestimoServico(
                usuarioRepositorio,
                livroRepositorio,
                emprestimoRepositorio,
                notificacao
        );

        System.out.println("=== Execucao com adaptador: " + adaptador + " ===");

        Livro livro = livroServico.cadastrarLivro(
                livroId,
                "Clean Code " + adaptador,
                "Robert C. Martin",
                "9780132350884-" + adaptador,
                2
        );
        System.out.println("Livro cadastrado: " + livro);

        Usuario usuario = usuarioServico.cadastrarUsuario(
                usuarioId,
                "Ana Silva " + adaptador,
                "ana.silva+" + adaptador.toLowerCase() + "@email.com",
                SituacaoUsuario.ATIVO
        );
        System.out.println("Usuario cadastrado: " + usuario);

        Emprestimo emprestimo = emprestimoServico.realizarEmprestimo(usuario.getId(), livro.getId());
        System.out.println("Emprestimo realizado: " + emprestimo);
        System.out.println("Quantidade disponivel apos emprestimo: " + livro.getQuantidadeDisponivel());

        System.out.println("Emprestimos ativos: " + emprestimoServico.listarEmprestimosAtivos());
        System.out.println("Emprestimos em atraso: " + emprestimoServico.verificarAtrasos());

        emprestimoServico.registrarDevolucao(emprestimo.getId());
        System.out.println("Devolucao registrada para o emprestimo ID: " + emprestimo.getId());
        System.out.println("Quantidade disponivel apos devolucao: " + livro.getQuantidadeDisponivel());
        System.out.println("Emprestimos ativos apos devolucao: " + emprestimoServico.listarEmprestimosAtivos());
        System.out.println();
    }
}
