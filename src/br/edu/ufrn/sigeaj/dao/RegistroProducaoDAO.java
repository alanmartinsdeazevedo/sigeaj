package br.edu.ufrn.sigeaj.dao;

import br.edu.ufrn.sigeaj.model.RegistroProducao;
import br.edu.ufrn.sigeaj.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade RegistroProducao.
 * CAMADA DE PERSISTÊNCIA - responsável por operações CRUD de registros de produção.
 */
public class RegistroProducaoDAO {

    /**
     * Insere um novo registro de produção no banco de dados.
     */
    public boolean inserir(RegistroProducao registro) throws SQLException {
        String sql = "INSERT INTO registro_producao (setor_id, data_registro, produto, quantidade, unidade) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, registro.getSetorId());
            stmt.setDate(2, Date.valueOf(registro.getDataRegistro()));
            stmt.setString(3, registro.getProduto());
            stmt.setBigDecimal(4, registro.getQuantidade());
            stmt.setString(5, registro.getUnidade());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        registro.setId(rs.getLong(1));
                    }
                }
            }

            return rowsAffected > 0;
        }
    }

    /**
     * Atualiza um registro de produção existente.
     */
    public boolean atualizar(RegistroProducao registro) throws SQLException {
        String sql = "UPDATE registro_producao SET setor_id = ?, data_registro = ?, produto = ?, " +
                    "quantidade = ?, unidade = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, registro.getSetorId());
            stmt.setDate(2, Date.valueOf(registro.getDataRegistro()));
            stmt.setString(3, registro.getProduto());
            stmt.setBigDecimal(4, registro.getQuantidade());
            stmt.setString(5, registro.getUnidade());
            stmt.setLong(6, registro.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove um registro de produção do banco de dados.
     */
    public boolean remover(Long id) throws SQLException {
        String sql = "DELETE FROM registro_producao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca um registro de produção por ID.
     */
    public RegistroProducao buscarPorId(Long id) throws SQLException {
        String sql = "SELECT r.*, s.nome AS setor_nome FROM registro_producao r " +
                    "INNER JOIN setor_produtivo s ON r.setor_id = s.id " +
                    "WHERE r.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairRegistroDoResultSet(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lista todos os registros de produção cadastrados.
     */
    public List<RegistroProducao> listarTodos() throws SQLException {
        List<RegistroProducao> registros = new ArrayList<>();
        String sql = "SELECT r.*, s.nome AS setor_nome FROM registro_producao r " +
                    "INNER JOIN setor_produtivo s ON r.setor_id = s.id " +
                    "ORDER BY r.data_registro DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                registros.add(extrairRegistroDoResultSet(rs));
            }
        }

        return registros;
    }

    /**
     * Lista registros de produção de um setor específico.
     */
    public List<RegistroProducao> listarPorSetor(Long setorId) throws SQLException {
        List<RegistroProducao> registros = new ArrayList<>();
        String sql = "SELECT r.*, s.nome AS setor_nome FROM registro_producao r " +
                    "INNER JOIN setor_produtivo s ON r.setor_id = s.id " +
                    "WHERE r.setor_id = ? ORDER BY r.data_registro DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, setorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(extrairRegistroDoResultSet(rs));
                }
            }
        }

        return registros;
    }

    /**
     * 4.b - FILTRO 3: Busca registros de produção por produto.
     */
    public List<RegistroProducao> buscarPorProduto(String produto) throws SQLException {
        List<RegistroProducao> registros = new ArrayList<>();
        String sql = "SELECT r.*, s.nome AS setor_nome FROM registro_producao r " +
                    "INNER JOIN setor_produtivo s ON r.setor_id = s.id " +
                    "WHERE LOWER(r.produto) LIKE LOWER(?) " +
                    "ORDER BY r.data_registro DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + produto + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(extrairRegistroDoResultSet(rs));
                }
            }
        }

        return registros;
    }

    /**
     * Método auxiliar para extrair um objeto RegistroProducao do ResultSet.
     */
    private RegistroProducao extrairRegistroDoResultSet(ResultSet rs) throws SQLException {
        RegistroProducao registro = new RegistroProducao();
        registro.setId(rs.getLong("id"));
        registro.setSetorId(rs.getLong("setor_id"));
        registro.setSetorNome(rs.getString("setor_nome"));
        registro.setDataRegistro(rs.getDate("data_registro").toLocalDate());
        registro.setProduto(rs.getString("produto"));
        registro.setQuantidade(rs.getBigDecimal("quantidade"));
        registro.setUnidade(rs.getString("unidade"));
        return registro;
    }
}
