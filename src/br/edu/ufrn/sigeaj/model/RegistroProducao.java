package br.edu.ufrn.sigeaj.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Classe de modelo que representa um registro de produção de um setor.
 */
public class RegistroProducao {
    private Long id;
    private Long setorId; // FK para SetorProdutivo
    private String setorNome;
    private LocalDate dataRegistro;
    private String produto;
    private BigDecimal quantidade;
    private String unidade;

    // Construtor padrão
    public RegistroProducao() {
    }

    // Construtor completo
    public RegistroProducao(Long id, Long setorId, LocalDate dataRegistro,
                           String produto, BigDecimal quantidade, String unidade) {
        this.id = id;
        this.setorId = setorId;
        this.dataRegistro = dataRegistro;
        this.produto = produto;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }

    // Construtor sem ID
    public RegistroProducao(Long setorId, LocalDate dataRegistro,
                           String produto, BigDecimal quantidade, String unidade) {
        this.setorId = setorId;
        this.dataRegistro = dataRegistro;
        this.produto = produto;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetorId() {
        return setorId;
    }

    public void setSetorId(Long setorId) {
        this.setorId = setorId;
    }

    public String getSetorNome() {
        return setorNome;
    }

    public void setSetorNome(String setorNome) {
        this.setorNome = setorNome;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public String toString() {
        return "RegistroProducao{" +
                "id=" + id +
                ", setorId=" + setorId +
                ", produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                ", unidade='" + unidade + '\'' +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
