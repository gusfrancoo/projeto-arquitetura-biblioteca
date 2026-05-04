package biblioteca.infraestrutura;

import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ServicoDeLog {
    private final Path arquivoLog;

    public ServicoDeLog(String caminhoArquivoLog) {
        this.arquivoLog = Paths.get(caminhoArquivoLog);
    }

    public void registrarEmprestimo(EmprestimoRealizadoEvento evento) {
        String linha = "[" + LocalDateTime.now() + "] EMPRESTIMO_REALIZADO " +
                "emprestimoId=" + evento.emprestimoId() +
                ", usuarioId=" + evento.usuarioId() +
                ", livroId=" + evento.livroId() +
                ", dataRetirada=" + evento.dataRetirada();
        escreverLinha(linha);
    }

    public void registrarDevolucao(DevolucaoRegistradaEvento evento) {
        String linha = "[" + LocalDateTime.now() + "] DEVOLUCAO_REGISTRADA " +
                "emprestimoId=" + evento.emprestimoId() +
                ", dataDevolucao=" + evento.dataDevolucao() +
                ", comAtraso=" + evento.comAtraso();
        escreverLinha(linha);
    }

    private void escreverLinha(String linha) {
        try {
            Path parent = arquivoLog.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(
                    arquivoLog,
                    linha + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao registrar log em " + arquivoLog, e);
        }
    }
}
