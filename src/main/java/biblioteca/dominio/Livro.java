package biblioteca.dominio;

import java.util.Objects;

public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeDisponivel;

    public Livro(Long id, String titulo, String autor, String isbn, int quantidadeDisponivel) {
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("A quantidade disponível não pode ser negativa.");
        }

        this.id = id;
        this.titulo = Objects.requireNonNull(titulo, "O título é obrigatório.");
        this.autor = Objects.requireNonNull(autor, "O autor é obrigatório.");
        this.isbn = Objects.requireNonNull(isbn, "O ISBN é obrigatório.");
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void realizarEmprestimo() {
        if (quantidadeDisponivel <= 0) {
            throw new IllegalStateException("Não há exemplares disponíveis para empréstimo.");
        }
        quantidadeDisponivel--;
    }

    public void registrarDevolucao() {
        quantidadeDisponivel++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Objects.requireNonNull(titulo, "O título é obrigatório.");
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = Objects.requireNonNull(autor, "O autor é obrigatório.");
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = Objects.requireNonNull(isbn, "O ISBN é obrigatório.");
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("A quantidade disponível não pode ser negativa.");
        }
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantidadeDisponivel=" + quantidadeDisponivel +
                '}';
    }
}
