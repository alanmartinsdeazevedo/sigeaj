package br.edu.ufrn.sigeaj.dao;

import br.edu.ufrn.sigeaj.model.PerfilUsuario;
import br.edu.ufrn.sigeaj.model.Usuario;
import br.edu.ufrn.sigeaj.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Usuario.
 */
public class UsuarioDAO {

    /**
     * Insere um novo usuário.
     */
    public boolean inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil().name());

            int rowsAffected = stmt.executeUpdate();

            // Recupera o ID
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getLong(1));
                    }
                }
            }

            return rowsAffected > 0;
        }
    }

    /**
     * Atualiza um usuário.
     */
    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, perfil = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil().name());
            stmt.setLong(5, usuario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove um usuário.
     */
    public boolean remover(Long id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca um usuário por ID.
     */
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairUsuarioDoResultSet(rs);
                }
            }
        }

        return null;
    }

    /**
     * Busca um usuário por email e senha (login).
     */
    public Usuario buscarPorEmailESenha(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairUsuarioDoResultSet(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lista todos os usuários.
     */
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }

        return usuarios;
    }

    /**
     * Verifica se já existe um usuário com o email informado.
     */
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Método auxiliar - extrair um objeto Usuario da consulta.
     */
    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(PerfilUsuario.valueOf(rs.getString("perfil")));
        return usuario;
    }
}
