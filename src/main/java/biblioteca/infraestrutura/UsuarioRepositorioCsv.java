package biblioteca.infraestrutura;

import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;

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

public class UsuarioRepositorioCsv implements UsuarioRepositorioPort {
    private final Path arquivo;

    public UsuarioRepositorioCsv(String caminhoArquivo) {
        this.arquivo = Paths.get(caminhoArquivo);
    }

    @Override
    public void salvar(Usuario usuario) {
        Map<Long, Usuario> usuarios = carregarMapa();
        usuarios.put(usuario.getId(), usuario);
        persistir(usuarios.values());
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(carregarMapa().get(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(carregarMapa().values());
    }

    @Override
    public void remover(Long id) {
        Map<Long, Usuario> usuarios = carregarMapa();
        usuarios.remove(id);
        persistir(usuarios.values());
    }

    private Map<Long, Usuario> carregarMapa() {
        Map<Long, Usuario> usuarios = new HashMap<>();
        if (!Files.exists(arquivo)) {
            return usuarios;
        }

        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes.length != 4) {
                    continue;
                }

                Long id = Long.parseLong(partes[0]);
                String nome = partes[1];
                String email = partes[2];
                SituacaoUsuario situacao = SituacaoUsuario.valueOf(partes[3]);

                usuarios.put(id, new Usuario(id, nome, email, situacao));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo CSV de usuarios: " + arquivo, e);
        }

        return usuarios;
    }

    private void persistir(Iterable<Usuario> usuarios) {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<Usuario> ordenados = new ArrayList<>();
            usuarios.forEach(ordenados::add);
            ordenados.sort(Comparator.comparing(Usuario::getId));

            List<String> linhas = new ArrayList<>();
            for (Usuario usuario : ordenados) {
                linhas.add(
                        usuario.getId() + ";" +
                                usuario.getNome() + ";" +
                                usuario.getEmail() + ";" +
                                usuario.getSituacao().name()
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
            throw new RuntimeException("Erro ao escrever arquivo CSV de usuarios: " + arquivo, e);
        }
    }
}
