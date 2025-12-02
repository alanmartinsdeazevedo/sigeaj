package br.edu.ufrn.sigeaj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL.
 * Implementa o padrão Factory para criação de conexões.
 * CAMADA DE PERSISTÊNCIA - responsável pelo acesso ao banco de dados.
 */
public class ConnectionFactory {

    // Configurações do banco de dados - podem ser movidas para arquivo properties
    // psql 'postgresql://neondb_owner:npg_gyXkt6H5fijF@ep-summer-violet-ahgro0gx-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'
    private static final String URL = "jdbc:postgresql://ep-summer-violet-ahgro0gx-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_gyXkt6H5fijF";

    /**
     * Cria e retorna uma nova conexão com o banco de dados.
     * @return Connection objeto de conexão com o PostgreSQL
     * @throws SQLException se houver erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carrega o driver do PostgreSQL (opcional no Java 21+, mas boa prática)
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado", e);
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     * @param conn conexão a ser fechada
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Testa a conexão com o banco de dados.
     * @return true se a conexão foi bem-sucedida
     */
    public static boolean testarConexao() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}
