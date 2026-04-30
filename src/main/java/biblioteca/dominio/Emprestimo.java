package biblioteca.dominio;

import java.time.LocalDate;
import java.util.Objects;

public class Emprestimo {
    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataPrevistaDevolucao;
    private SituacaoEmprestimo situacao;

    public Emprestimo(Long id, Livro livro, Usuario usuario, LocalDate dataRetirada, LocalDate dataPrevistaDevolucao, SituacaoEmprestimo situacao) {
        this.id = id;
        this.livro = Objects.requireNonNull(livro, "O livro é obrigatório.");
        this.usuario = Objects.requireNonNull(usuario, "O usuário é obrigatório.");
        this.dataRetirada = Objects.requireNonNull(dataRetirada, "A data de retirada é obrigatória.");
        this.dataPrevistaDevolucao = Objects.requireNonNull(dataPrevistaDevolucao, "A data prevista de devolução é obrigatória.");
        this.situacao = Objects.requireNonNull(situacao, "A situação do empréstimo é obrigatória.");
    }

    public boolean estaAtivo() {
        return SituacaoEmprestimo.ATIVO.equals(situacao) || SituacaoEmprestimo.ATRASADO.equals(situacao);
    }

    public boolean estaAtrasado(LocalDate dataReferencia) {
        Objects.requireNonNull(dataReferencia, "A data de referência é obrigatória.");
        return estaAtivo() && dataReferencia.isAfter(dataPrevistaDevolucao);
    }

    public void marcarComoAtrasado() {
        if (SituacaoEmprestimo.ATIVO.equals(situacao)) {
            situacao = SituacaoEmprestimo.ATRASADO;
        }
    }

    public void registrarDevolucao() {
        if (SituacaoEmprestimo.DEVOLVIDO.equals(situacao)) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }
        livro.registrarDevolucao();
        situacao = SituacaoEmprestimo.DEVOLVIDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = Objects.requireNonNull(livro, "O livro é obrigatório.");
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = Objects.requireNonNull(usuario, "O usuário é obrigatório.");
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = Objects.requireNonNull(dataRetirada, "A data de retirada é obrigatória.");
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = Objects.requireNonNull(dataPrevistaDevolucao, "A data prevista de devolução é obrigatória.");
    }

    public SituacaoEmprestimo getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoEmprestimo situacao) {
        this.situacao = Objects.requireNonNull(situacao, "A situação do empréstimo é obrigatória.");
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", livro=" + livro.getTitulo() +
                ", usuario=" + usuario.getNome() +
                ", dataRetirada=" + dataRetirada +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", situacao=" + situacao +
                '}';
    }
}
