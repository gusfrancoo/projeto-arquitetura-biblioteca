package biblioteca.aplicacao.porta.saida;

import biblioteca.dominio.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroRepositorioPort {
    void salvar(Livro livro);

    Optional<Livro> buscarPorId(Long id);

    List<Livro> listarTodos();

    void remover(Long id);
}
