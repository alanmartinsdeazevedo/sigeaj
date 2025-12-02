package br.edu.ufrn.sigeaj.model;

/**
 * Enum que representa os perfis de acesso do sistema.
 * Demonstra o uso de enumerações em POO.
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
