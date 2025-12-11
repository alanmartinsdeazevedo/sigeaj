package br.edu.ufrn.sigeaj.dao;

import br.edu.ufrn.sigeaj.model.Atividade;
import br.edu.ufrn.sigeaj.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Atividade. - operações CRUD de atividades.
 */
public class AtividadeDAO {

    /**
     * Insere uma nova atividade no banco de dados.
     */
    public boolean inserir(Atividade atividade) throws SQLException {
        String sql = "INSERT INTO atividade (setor_id, tipo, descricao, data_execucao) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, atividade.getSetorId());
            stmt.setString(2, atividade.getTipo());
            stmt.setString(3, atividade.getDescricao());
            stmt.setDate(4, Date.valueOf(atividade.getDataExecucao()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        atividade.setId(rs.getLong(1));
                    }
                }
            }

            return rowsAffected > 0;
        }
    }

    /**
     * Atualiza uma atividade existente.
     */
    public boolean atualizar(Atividade atividade) throws SQLException {
        String sql = "UPDATE atividade SET setor_id = ?, tipo = ?, descricao = ?, data_execucao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, atividade.getSetorId());
            stmt.setString(2, atividade.getTipo());
            stmt.setString(3, atividade.getDescricao());
            stmt.setDate(4, Date.valueOf(atividade.getDataExecucao()));
            stmt.setLong(5, atividade.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove uma atividade do banco de dados.
     */
    public boolean remover(Long id) throws SQLException {
        String sql = "DELETE FROM atividade WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca uma atividade por ID.
     */
    public Atividade buscarPorId(Long id) throws SQLException {
        String sql = "SELECT a.*, s.nome AS setor_nome FROM atividade a " +
                    "INNER JOIN setor_produtivo s ON a.setor_id = s.id " +
                    "WHERE a.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairAtividadeDoResultSet(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lista todas as atividades cadastradas.
     * Usa JOIN para trazer o nome do setor junto.
     */
    public List<Atividade> listarTodos() throws SQLException {
        List<Atividade> atividades = new ArrayList<>();
        String sql = "SELECT a.*, s.nome AS setor_nome FROM atividade a " +
                    "INNER JOIN setor_produtivo s ON a.setor_id = s.id " +
                    "ORDER BY a.data_execucao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                atividades.add(extrairAtividadeDoResultSet(rs));
            }
        }

        return atividades;
    }

    /**
     * Lista atividades de um setor específico.
     */
    public List<Atividade> listarPorSetor(Long setorId) throws SQLException {
        List<Atividade> atividades = new ArrayList<>();
        String sql = "SELECT a.*, s.nome AS setor_nome FROM atividade a " +
                    "INNER JOIN setor_produtivo s ON a.setor_id = s.id " +
                    "WHERE a.setor_id = ? ORDER BY a.data_execucao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, setorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    atividades.add(extrairAtividadeDoResultSet(rs));
                }
            }
        }

        return atividades;
    }

    /**
     * 4.b - FILTRO 2: Busca atividades por período de data.
     */
    public List<Atividade> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        List<Atividade> atividades = new ArrayList<>();
        String sql = "SELECT a.*, s.nome AS setor_nome FROM atividade a " +
                    "INNER JOIN setor_produtivo s ON a.setor_id = s.id " +
                    "WHERE a.data_execucao BETWEEN ? AND ? " +
                    "ORDER BY a.data_execucao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    atividades.add(extrairAtividadeDoResultSet(rs));
                }
            }
        }

        return atividades;
    }

    /**
     * 4.b - FILTRO ADICIONAL: Busca atividades por tipo.
     */
    public List<Atividade> buscarPorTipo(String tipo) throws SQLException {
        List<Atividade> atividades = new ArrayList<>();
        String sql = "SELECT a.*, s.nome AS setor_nome FROM atividade a " +
                    "INNER JOIN setor_produtivo s ON a.setor_id = s.id " +
                    "WHERE LOWER(a.tipo) LIKE LOWER(?) " +
                    "ORDER BY a.data_execucao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + tipo + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    atividades.add(extrairAtividadeDoResultSet(rs));
                }
            }
        }

        return atividades;
    }

    /**
     * Método auxiliar para extrair um objeto Atividade.
     */
    private Atividade extrairAtividadeDoResultSet(ResultSet rs) throws SQLException {
        Atividade atividade = new Atividade();
        atividade.setId(rs.getLong("id"));
        atividade.setSetorId(rs.getLong("setor_id"));
        atividade.setSetorNome(rs.getString("setor_nome"));
        atividade.setTipo(rs.getString("tipo"));
        atividade.setDescricao(rs.getString("descricao"));
        atividade.setDataExecucao(rs.getDate("data_execucao").toLocalDate());
        return atividade;
    }
}
