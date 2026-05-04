package biblioteca.infraestrutura;

import biblioteca.aplicacao.porta.saida.UsuarioRepositorioPort;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;

import java.time.LocalDate;

public class ServicoDeNotificacao {
    private static final int PRAZO_PADRAO_DIAS = 7;

    private final UsuarioRepositorioPort usuarioRepositorio;

    public ServicoDeNotificacao(UsuarioRepositorioPort usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void notificarEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        Usuario usuario = usuarioRepositorio.buscarPorId(evento.usuarioId())
                .orElse(null);

        String nomeUsuario = usuario != null ? usuario.getNome() : "usuario " + evento.usuarioId();
        LocalDate dataPrevista = evento.dataRetirada().plusDays(PRAZO_PADRAO_DIAS);

        System.out.println(
                "[EVENTO][NOTIFICACAO] Emprestimo " + evento.emprestimoId() +
                        " para " + nomeUsuario +
                        ". Devolucao prevista em " + dataPrevista
        );
    }
}
