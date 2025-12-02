package br.edu.ufrn.sigeaj.model;

/**
 * Classe de modelo que representa um setor produtivo da EAJ.
 * Exemplos: Piscicultura, Avicultura, Bovinos, Suínos, Hortaliças.
 * Demonstra ENCAPSULAMENTO e COMPOSIÇÃO (pode ter relacionamento com Usuario).
 */
public class SetorProdutivo {
    private Long id;
    private String nome;
    private String descricao;
    private String responsavel;

    // Construtor padrão
    public SetorProdutivo() {
    }

    // Construtor com parâmetros
    public SetorProdutivo(Long id, String nome, String descricao, String responsavel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.responsavel = responsavel;
    }

    // Construtor sem ID (para inserções)
    public SetorProdutivo(String nome, String descricao, String responsavel) {
        this.nome = nome;
        this.descricao = descricao;
        this.responsavel = responsavel;
    }

    // Getters e Setters
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
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public String toString() {
        return nome; // Útil para exibição em ComboBox
    }
}
