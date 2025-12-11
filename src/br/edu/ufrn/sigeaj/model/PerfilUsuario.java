package br.edu.ufrn.sigeaj.model;

/**
 * Enum dos perfis de acesso do sistema.
 */
public enum PerfilUsuario {
    ADMIN("Administrador"),
    OPERADOR("Operador");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
