package biblioteca.apresentacao;

import biblioteca.aplicacao.EmprestimoServico;
import biblioteca.aplicacao.LivroServico;
import biblioteca.aplicacao.UsuarioServico;
import biblioteca.aplicacao.porta.entrada.PortaEmprestimo;
import biblioteca.aplicacao.porta.saida.EmprestimoRepositorioPort;
import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.aplicacao.porta.saida.PortaNotificacao;
import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.EventBus;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.EmprestimoRepositorioCsv;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.LivroRepositorioCsv;
import biblioteca.infraestrutura.NotificacaoConsole;
import biblioteca.infraestrutura.ServicoDeLog;
import biblioteca.infraestrutura.ServicoDeNotificacao;
import biblioteca.infraestrutura.UsuarioRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorioCsv;

public class Main {
    public static void main(String[] args) {
        PortaNotificacao notificacao = new NotificacaoConsole();

        executarFluxo(
                "Memoria",
                new LivroRepositorio(),
                new UsuarioRepositorio(),
                new EmprestimoRepositorio(),
                notificacao,
                1L,
                1L
        );

        LivroRepositorioPort livroRepositorioCsv = new LivroRepositorioCsv("livros.csv");
        UsuarioRepositorioPort usuarioRepositorioCsv = new UsuarioRepositorioCsv("usuarios.csv");
        EmprestimoRepositorioPort emprestimoRepositorioCsv =
                new EmprestimoRepositorioCsv("emprestimos.csv", livroRepositorioCsv, usuarioRepositorioCsv);

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
        EventBus<EmprestimoRealizadoEvento> eventBusEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> eventBusDevolucao = new EventBus<>();

        ServicoDeNotificacao servicoDeNotificacao = new ServicoDeNotificacao(usuarioRepositorio);
        ServicoDeLog servicoDeLog = new ServicoDeLog("biblioteca.log");

        eventBusEmprestimo.assinar(servicoDeNotificacao::notificarEmprestimoRealizado);
        eventBusEmprestimo.assinar(servicoDeLog::registrarEmprestimo);
        eventBusDevolucao.assinar(servicoDeLog::registrarDevolucao);

        LivroServico livroServico = new LivroServico(livroRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio);
        PortaEmprestimo emprestimoServico = new EmprestimoServico(
                usuarioRepositorio,
                livroRepositorio,
                emprestimoRepositorio,
                notificacao,
                eventBusEmprestimo,
                eventBusDevolucao
        );

        System.out.println("=== Execucao com adaptador: " + adaptador + " ===");
        System.out.println("LivroRepo: " + livroRepositorio.getClass().getSimpleName());
        System.out.println("UsuarioRepo: " + usuarioRepositorio.getClass().getSimpleName());
        System.out.println("EmprestimoRepo: " + emprestimoRepositorio.getClass().getSimpleName());

        Livro livro = livroServico.cadastrarLivro(
                livroId,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                2
        );
        System.out.println("Livro cadastrado: " + livro);

        Usuario usuario = usuarioServico.cadastrarUsuario(
                usuarioId,
                "Ana Silva",
                "ana.silva@email.com",
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
