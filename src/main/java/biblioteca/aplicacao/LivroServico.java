package biblioteca.aplicacao;

import biblioteca.dominio.Livro;
import biblioteca.infraestrutura.LivroRepositorio;

import java.util.List;

public class LivroServico {
    private final LivroRepositorio livroRepositorio;

    public LivroServico(LivroRepositorio livroRepositorio) {
        this.livroRepositorio = livroRepositorio;
    }

    public Livro cadastrarLivro(Long id, String titulo, String autor, String isbn, int quantidadeDisponivel) {
        Livro livro = new Livro(id, titulo, autor, isbn, quantidadeDisponivel);
        livroRepositorio.salvar(livro);
        return livro;
    }

    public Livro buscarPorId(Long id) {
        return livroRepositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado: " + id));
    }

    public List<Livro> listarTodos() {
        return livroRepositorio.listarTodos();
    }

    public void remover(Long id) {
        livroRepositorio.remover(id);
    }
}
