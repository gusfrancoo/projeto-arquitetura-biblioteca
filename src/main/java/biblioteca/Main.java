package biblioteca;

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
        EmprestimoServico emprestimoServico = new EmprestimoServico(
                usuarioRepositorio,
                livroRepositorio,
                emprestimoRepositorio
        );

        System.out.println("=== Sistema de Gerenciamento de Biblioteca - Etapa 2 (Hexagonal) ===");

        Livro livro = livroServico.cadastrarLivro(1L, "Clean Code", "Robert C. Martin", "9780132350884", 2);
        System.out.println("Livro cadastrado: " + livro);

        Usuario usuario = usuarioServico.cadastrarUsuario(1L, "Ana Silva", "ana.silva@email.com", SituacaoUsuario.ATIVO);
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
    }
}
