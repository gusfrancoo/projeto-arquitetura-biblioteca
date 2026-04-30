package biblioteca.infraestrutura;

import biblioteca.dominio.Emprestimo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmprestimoRepositorio {
    private final Map<Long, Emprestimo> emprestimos = new HashMap<>();

    public void salvar(Emprestimo emprestimo) {
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(emprestimos.get(id));
    }

    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(emprestimos.values());
    }

    public void remover(Long id) {
        emprestimos.remove(id);
    }
}
