package br.edu.ufrn.sigeaj.dao;

import br.edu.ufrn.sigeaj.model.SetorProdutivo;
import br.edu.ufrn.sigeaj.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade SetorProdutivo.
 * CAMADA DE PERSISTÊNCIA - responsável por operações CRUD de setores produtivos.
 */
public class SetorProdutivoDAO {

    /**
     * Inserir novo setor produtivo.
     */
    public boolean inserir(SetorProdutivo setor) throws SQLException {
        String sql = "INSERT INTO setor_produtivo (nome, descricao, responsavel) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            stmt.setString(3, setor.getResponsavel());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        setor.setId(rs.getLong(1));
                    }
                }
            }

            return rowsAffected > 0;
        }
    }

    /**
     * Atualizar setor produtivo existente.
     */
    public boolean atualizar(SetorProdutivo setor) throws SQLException {
        String sql = "UPDATE setor_produtivo SET nome = ?, descricao = ?, responsavel = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            stmt.setString(3, setor.getResponsavel());
            stmt.setLong(4, setor.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove setor produtivo do banco de dados.
     */
    public boolean remover(Long id) throws SQLException {
        String sql = "DELETE FROM setor_produtivo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca setor produtivo por ID.
     */
    public SetorProdutivo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM setor_produtivo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairSetorDoResultSet(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lista todos os setores produtivos.
     */
    public List<SetorProdutivo> listarTodos() throws SQLException {
        List<SetorProdutivo> setores = new ArrayList<>();
        String sql = "SELECT * FROM setor_produtivo ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                setores.add(extrairSetorDoResultSet(rs));
            }
        }

        return setores;
    }

    /**
     * Verifica se existe um setor com o nome informado.
     */
    public boolean existeNome(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM setor_produtivo WHERE LOWER(nome) = LOWER(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Verifica se já existe um setor com o nome informado, excluindo um ID específico
     */
    public boolean existeNomeExceto(String nome, Long idExceto) throws SQLException {
        String sql = "SELECT COUNT(*) FROM setor_produtivo WHERE LOWER(nome) = LOWER(?) AND id != ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setLong(2, idExceto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * 4.b - FILTRO 1: Busca setores por nome (pesquisa parcial).
     */
    public List<SetorProdutivo> buscarPorNome(String nome) throws SQLException {
        List<SetorProdutivo> setores = new ArrayList<>();
        String sql = "SELECT * FROM setor_produtivo WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    setores.add(extrairSetorDoResultSet(rs));
                }
            }
        }

        return setores;
    }

    /**
     * Método auxiliar extrair um objeto SetorProdutivo.
     */
    private SetorProdutivo extrairSetorDoResultSet(ResultSet rs) throws SQLException {
        SetorProdutivo setor = new SetorProdutivo();
        setor.setId(rs.getLong("id"));
        setor.setNome(rs.getString("nome"));
        setor.setDescricao(rs.getString("descricao"));
        setor.setResponsavel(rs.getString("responsavel"));
        return setor;
    }
}
