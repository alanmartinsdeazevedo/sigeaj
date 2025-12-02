package br.edu.ufrn.sigeaj;

import br.edu.ufrn.sigeaj.util.ConnectionFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Classe principal do sistema SIGEAJ.
 * Demonstra o uso de JavaFX Application com padrão MVC.
 *
 * FLUXO DE EXECUÇÃO:
 * 1. Inicia a aplicação JavaFX
 * 2. Testa a conexão com o banco de dados
 * 3. Carrega a tela de login (VIEW - login.fxml)
 * 4. O LoginController (CONTROLLER) gerencia a lógica da tela
 * 5. O UsuarioService (SERVICE) valida as credenciais
 * 6. O UsuarioDAO (DAO) acessa o banco de dados
 */
public class Main extends Application {

    /**
     * Método principal que inicia a aplicação JavaFX.
     * É chamado automaticamente pelo JavaFX após o método main().
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Testa a conexão com o banco de dados antes de iniciar
            if (!testarConexaoBanco()) {
                exibirErroConexao();
                return;
            }

            // Carrega o arquivo FXML da tela de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufrn/sigeaj/view/login.fxml"));
            Parent root = loader.load();

            // Configura a cena e o palco (janela)
            Scene scene = new Scene(root);
            primaryStage.setTitle("SIGEAJ - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("SIGEAJ iniciado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao iniciar o sistema");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Testa a conexão com o banco de dados PostgreSQL.
     * @return true se a conexão foi bem-sucedida
     */
    private boolean testarConexaoBanco() {
        System.out.println("Testando conexão com o banco de dados...");
        boolean conectado = ConnectionFactory.testarConexao();

        if (conectado) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } else {
            System.err.println("Falha ao conectar com o banco de dados!");
        }

        return conectado;
    }

    /**
     * Exibe mensagem de erro caso não consiga conectar ao banco.
     */
    private void exibirErroConexao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Conexão");
        alert.setHeaderText("Não foi possível conectar ao banco de dados");
        alert.setContentText(
            "Verifique se:\n" +
            "1. O PostgreSQL está instalado e rodando\n" +
            "2. O banco de dados 'sigeaj' foi criado\n" +
            "3. As credenciais em ConnectionFactory estão corretas\n" +
            "4. O driver JDBC do PostgreSQL está no classpath\n\n" +
            "Consulte o arquivo database/README.md para instruções."
        );
        alert.showAndWait();
    }

    /**
     * Método de entrada da aplicação Java.
     * Lança a aplicação JavaFX.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando SIGEAJ - Sistema de Gestão de Setores Produtivos da EAJ");
        System.out.println("Versão: 1.0");
        System.out.println("========================================");

        // Inicia a aplicação JavaFX
        launch(args);
    }
}
