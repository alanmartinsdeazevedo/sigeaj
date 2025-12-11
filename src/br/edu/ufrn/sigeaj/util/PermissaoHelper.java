package br.edu.ufrn.sigeaj.util;

import br.edu.ufrn.sigeaj.controller.LoginController;
import br.edu.ufrn.sigeaj.model.PerfilUsuario;
import br.edu.ufrn.sigeaj.model.Usuario;

/**
 * Classe utilitária para verificar permissões dos usuários.
 * Implementa controle de acesso baseado em perfil (RBAC - Role-Based Access Control).
 */
public class PermissaoHelper {

    /**
     * Verifica se o usuário logado é um ADMINISTRADOR.
     */
    public static boolean isAdmin() {
        Usuario usuario = LoginController.getUsuarioLogado();
        return usuario != null && usuario.getPerfil() == PerfilUsuario.ADMIN;
    }

    /**
     * Verifica se o usuário logado é um OPERADOR.
     */
    public static boolean isOperador() {
        Usuario usuario = LoginController.getUsuarioLogado();
        return usuario != null && usuario.getPerfil() == PerfilUsuario.OPERADOR;
    }

    /**
     * Verifica se o usuário tem permissão para EXCLUIR registros.
     */
    public static boolean podeExcluir() {
        return isAdmin();
    }

    /**
     * Verifica se o usuário tem permissão para CADASTRAR registros.
     */
    public static boolean podeCadastrar() {
        return isAdmin() || isOperador();
    }

    /**
     * Verifica se o usuário tem permissão para EDITAR registros.
     */
    public static boolean podeEditar() {
        return isAdmin() || isOperador();
    }

    /**
     * Mensagem de erro padrão para permissão negada.
     */
    public static String getMensagemPermissaoNegada() {
        return "Você não tem permissão para realizar esta ação.\n" +
               "Apenas usuários ADMINISTRADORES podem excluir registros.";
    }

    /**
     * Obter nome do perfil do usuário.
     */
    public static String getPerfilUsuarioLogado() {
        Usuario usuario = LoginController.getUsuarioLogado();
        return usuario != null ? usuario.getPerfil().getDescricao() : "Desconhecido";
    }
}
