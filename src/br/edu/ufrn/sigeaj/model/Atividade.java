package br.edu.ufrn.sigeaj.model;

import java.time.LocalDate;

/**
 * Classe de modelo que representa uma atividade realizada em um setor produtivo.
 * Exemplos: Alimentação, Limpeza, Vacinação.
 * Demonstra ASSOCIAÇÃO entre classes (Atividade tem relação com SetorProdutivo).
 */
public class Atividade {
    private Long id;
    private Long setorId; // Chave estrangeira para SetorProdutivo
    private String setorNome; // Para exibição na interface (não persistido separadamente)
    private String tipo;
    private String descricao;
    private LocalDate dataExecucao;

    // Construtor padrão
    public Atividade() {
    }

    // Construtor completo
    public Atividade(Long id, Long setorId, String tipo, String descricao, LocalDate dataExecucao) {
        this.id = id;
        this.setorId = setorId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataExecucao = dataExecucao;
    }

    // Construtor sem ID
    public Atividade(Long setorId, String tipo, String descricao, LocalDate dataExecucao) {
        this.setorId = setorId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataExecucao = dataExecucao;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    @Override
    public String toString() {
        return "Atividade{" +
                "id=" + id +
                ", setorId=" + setorId +
                ", tipo='" + tipo + '\'' +
                ", dataExecucao=" + dataExecucao +
                '}';
    }
}
