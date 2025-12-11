package br.edu.ufrn.sigeaj;

import br.edu.ufrn.sigeaj.util.ConnectionFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Classe principal do sistema.
 */
public class Main extends Application {

    /**
     * Método principal que inicia a aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Testa a conexão com o banco de dados
            if (!testarConexaoBanco()) {
                exibirErroConexao();
                return;
            }

            // Carrega o arquivo Splash Screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufrn/sigeaj/view/splash.fxml"));
            Parent root = loader.load();

            // Configura a cena e palco
            Scene scene = new Scene(root);
            primaryStage.setTitle("SIGEAJ - Sistema de Gestão de Setores Produtivos");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

            System.out.println("SIGEAJ iniciado com sucesso!");
            System.out.println("Exibindo tela inicial (Splash Screen)...");

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
            "Verifique a conexão com o banco de dados\n"
        );
        alert.showAndWait();
    }

    /**
     * Método de entrada da aplicação Java.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando SIGEAJ - Sistema de Gestão de Setores Produtivos da EAJ");
        System.out.println("Versão: 1.0");
        System.out.println("========================================");

        launch(args);
    }
}
