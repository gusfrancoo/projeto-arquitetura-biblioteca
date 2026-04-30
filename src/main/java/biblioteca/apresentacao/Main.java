package biblioteca.apresentacao;

import biblioteca.aplicacao.EmprestimoServico;
import biblioteca.aplicacao.LivroServico;
import biblioteca.aplicacao.UsuarioServico;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorio;

public class Main {
    public static void main(String[] args) {
        LivroRepositorio livroRepositorio = new LivroRepositorio();
        UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
        EmprestimoRepositorio emprestimoRepositorio = new EmprestimoRepositorio();

        LivroServico livroServico = new LivroServico(livroRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio);
        EmprestimoServico emprestimoServico = new EmprestimoServico(usuarioRepositorio, livroRepositorio, emprestimoRepositorio);

        System.out.println("=== Sistema de Gerenciamento de Biblioteca - Etapa 1 ===");

        Livro livro = livroServico.cadastrarLivro(1L, "Clean Code", "Robert C. Martin", "9780132350884", 2);
        System.out.println("Livro cadastrado: " + livro);

        Usuario usuario = usuarioServico.cadastrarUsuario(1L, "Ana Silva", "ana.silva@email.com", SituacaoUsuario.ATIVO);
        System.out.println("Usuário cadastrado: " + usuario);

        Emprestimo emprestimo = emprestimoServico.realizarEmprestimo(usuario.getId(), livro.getId());
        System.out.println("Empréstimo realizado: " + emprestimo);
        System.out.println("Quantidade disponível após empréstimo: " + livro.getQuantidadeDisponivel());

        System.out.println("Empréstimos ativos: " + emprestimoServico.listarEmprestimosAtivos());
        System.out.println("Empréstimos em atraso: " + emprestimoServico.verificarAtrasos());

        emprestimoServico.registrarDevolucao(emprestimo.getId());
        System.out.println("Devolução registrada para o empréstimo ID: " + emprestimo.getId());
        System.out.println("Quantidade disponível após devolução: " + livro.getQuantidadeDisponivel());
        System.out.println("Empréstimos ativos após devolução: " + emprestimoServico.listarEmprestimosAtivos());
    }
}
