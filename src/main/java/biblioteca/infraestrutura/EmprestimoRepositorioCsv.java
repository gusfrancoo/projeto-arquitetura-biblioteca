package biblioteca.infraestrutura;

import biblioteca.aplicacao.porta.saida.EmprestimoRepositorioPort;
import biblioteca.aplicacao.porta.saida.LivroRepositorioPort;
import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmprestimoRepositorioCsv implements EmprestimoRepositorioPort {
    private final Path arquivo;
    private final LivroRepositorioPort livroRepositorio;
    private final UsuarioRepositorioPort usuarioRepositorio;

    public EmprestimoRepositorioCsv(
            String caminhoArquivo,
            LivroRepositorioPort livroRepositorio,
            UsuarioRepositorioPort usuarioRepositorio
    ) {
        this.arquivo = Paths.get(caminhoArquivo);
        this.livroRepositorio = livroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        Map<Long, Emprestimo> emprestimos = carregarMapa();
        emprestimos.put(emprestimo.getId(), emprestimo);
        persistir(emprestimos.values());
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(carregarMapa().get(id));
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(carregarMapa().values());
    }

    @Override
    public void remover(Long id) {
        Map<Long, Emprestimo> emprestimos = carregarMapa();
        emprestimos.remove(id);
        persistir(emprestimos.values());
    }

    private Map<Long, Emprestimo> carregarMapa() {
        Map<Long, Emprestimo> emprestimos = new HashMap<>();
        if (!Files.exists(arquivo)) {
            return emprestimos;
        }

        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] partes = linha.split(";", -1);
                if (partes.length != 6) {
                    continue;
                }

                Long id = Long.parseLong(partes[0]);
                Long livroId = Long.parseLong(partes[1]);
                Long usuarioId = Long.parseLong(partes[2]);
                LocalDate dataRetirada = LocalDate.parse(partes[3]);
                LocalDate dataPrevista = LocalDate.parse(partes[4]);
                SituacaoEmprestimo situacao = SituacaoEmprestimo.valueOf(partes[5]);

                Optional<Livro> livroOpt = livroRepositorio.buscarPorId(livroId);
                Optional<Usuario> usuarioOpt = usuarioRepositorio.buscarPorId(usuarioId);
                if (livroOpt.isEmpty() || usuarioOpt.isEmpty()) {
                    continue;
                }

                Emprestimo emprestimo = new Emprestimo(
                        id,
                        livroOpt.get(),
                        usuarioOpt.get(),
                        dataRetirada,
                        dataPrevista,
                        situacao
                );
                emprestimos.put(id, emprestimo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo CSV de emprestimos: " + arquivo, e);
        }

        return emprestimos;
    }

    private void persistir(Iterable<Emprestimo> emprestimos) {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<Emprestimo> ordenados = new ArrayList<>();
            emprestimos.forEach(ordenados::add);
            ordenados.sort(Comparator.comparing(Emprestimo::getId));

            List<String> linhas = new ArrayList<>();
            for (Emprestimo emprestimo : ordenados) {
                linhas.add(
                        emprestimo.getId() + ";" +
                                emprestimo.getLivro().getId() + ";" +
                                emprestimo.getUsuario().getId() + ";" +
                                emprestimo.getDataRetirada() + ";" +
                                emprestimo.getDataPrevistaDevolucao() + ";" +
                                emprestimo.getSituacao().name()
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
            throw new RuntimeException("Erro ao escrever arquivo CSV de emprestimos: " + arquivo, e);
        }
    }
}
