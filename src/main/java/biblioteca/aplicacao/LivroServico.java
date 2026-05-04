package biblioteca.aplicacao;

import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.dominio.Livro;

import java.util.List;

public class LivroServico {
    private final LivroRepositorioPort livroRepositorio;

    public LivroServico(LivroRepositorioPort livroRepositorio) {
        this.livroRepositorio = livroRepositorio;
    }

    public Livro cadastrarLivro(Long id, String titulo, String autor, String isbn, int quantidadeDisponivel) {
        Livro livro = new Livro(id, titulo, autor, isbn, quantidadeDisponivel);
        livroRepositorio.salvar(livro);
        return livro;
    }

    public Livro buscarPorId(Long id) {
        return livroRepositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro nao encontrado: " + id));
    }

    public List<Livro> listarTodos() {
        return livroRepositorio.listarTodos();
    }

    public void remover(Long id) {
        livroRepositorio.remover(id);
    }
}
