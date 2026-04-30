package biblioteca.dominio;

import java.util.Objects;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private SituacaoUsuario situacao;

    public Usuario(Long id, String nome, String email, SituacaoUsuario situacao) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "O nome é obrigatório.");
        this.email = Objects.requireNonNull(email, "O e-mail é obrigatório.");
        this.situacao = Objects.requireNonNull(situacao, "A situação do usuário é obrigatória.");
    }

    public boolean estaAtivo() {
        return SituacaoUsuario.ATIVO.equals(situacao);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome é obrigatório.");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "O e-mail é obrigatório.");
    }

    public SituacaoUsuario getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoUsuario situacao) {
        this.situacao = Objects.requireNonNull(situacao, "A situação do usuário é obrigatória.");
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", situacao=" + situacao +
                '}';
    }
}
