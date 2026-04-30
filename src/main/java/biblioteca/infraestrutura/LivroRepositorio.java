package biblioteca.infraestrutura;

import biblioteca.dominio.Livro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LivroRepositorio {
    private final Map<Long, Livro> livros = new HashMap<>();

    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(livros.get(id));
    }

    public List<Livro> listarTodos() {
        return new ArrayList<>(livros.values());
    }

    public void remover(Long id) {
        livros.remove(id);
    }
}
