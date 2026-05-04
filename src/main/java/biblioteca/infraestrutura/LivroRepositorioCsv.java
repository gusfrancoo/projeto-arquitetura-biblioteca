package biblioteca.infraestrutura;

import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.dominio.Livro;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LivroRepositorioCsv implements LivroRepositorioPort {
    private final Path arquivo;

    public LivroRepositorioCsv(String caminhoArquivo) {
        this.arquivo = Paths.get(caminhoArquivo);
    }

    @Override
    public void salvar(Livro livro) {
        Map<Long, Livro> livros = carregarMapa();
        livros.put(livro.getId(), livro);
        persistir(livros.values());
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(carregarMapa().get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(carregarMapa().values());
    }

    @Override
    public void remover(Long id) {
        Map<Long, Livro> livros = carregarMapa();
        livros.remove(id);
        persistir(livros.values());
    }

    private Map<Long, Livro> carregarMapa() {
        Map<Long, Livro> livros = new HashMap<>();
        if (!Files.exists(arquivo)) {
            return livros;
        }

        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes.length != 5) {
                    continue;
                }

                Long id = Long.parseLong(partes[0]);
                String titulo = partes[1];
                String autor = partes[2];
                String isbn = partes[3];
                int quantidadeDisponivel = Integer.parseInt(partes[4]);

                livros.put(id, new Livro(id, titulo, autor, isbn, quantidadeDisponivel));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo CSV de livros: " + arquivo, e);
        }

        return livros;
    }

    private void persistir(Iterable<Livro> livros) {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<Livro> ordenados = new ArrayList<>();
            livros.forEach(ordenados::add);
            ordenados.sort(Comparator.comparing(Livro::getId));

            List<String> linhas = new ArrayList<>();
            for (Livro livro : ordenados) {
                linhas.add(
                        livro.getId() + ";" +
                                livro.getTitulo() + ";" +
                                livro.getAutor() + ";" +
                                livro.getIsbn() + ";" +
                                livro.getQuantidadeDisponivel()
                );
            }

            Files.write(
                    arquivo,
                    linhas,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo CSV de livros: " + arquivo, e);
        }
    }
}
