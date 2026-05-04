package biblioteca.infraestrutura;

import biblioteca.aplicacao.porta.saida.PortaNotificacao;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Usuario;

public class NotificacaoConsole implements PortaNotificacao {
    @Override
    public void notificarAtraso(Usuario usuario, Emprestimo emprestimo) {
        System.out.println(
                "[NOTIFICACAO] Usuario " + usuario.getNome() +
                        " esta com emprestimo em atraso. Emprestimo ID: " + emprestimo.getId() +
                        ", data prevista: " + emprestimo.getDataPrevistaDevolucao()
        );
    }
}
