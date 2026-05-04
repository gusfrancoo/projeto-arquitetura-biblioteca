package biblioteca.aplicacao;

import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;

import java.util.List;

public class UsuarioServico {
    private final UsuarioRepositorioPort usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorioPort usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario cadastrarUsuario(Long id, String nome, String email, SituacaoUsuario situacao) {
        Usuario usuario = new Usuario(id, nome, email, situacao);
        usuarioRepositorio.salvar(usuario);
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.listarTodos();
    }

    public void remover(Long id) {
        usuarioRepositorio.remover(id);
    }
}
