package br.edu.ufrn.sigeaj.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller Splash Screen.
 */
public class SplashController {

    @FXML
    private Label lblNomeDesenvolvedor;

    @FXML
    private Label lblVersao;

    @FXML
    private Button btnContinuar;

    /**
     * Inicializa a tela splash.
     */
    @FXML
    public void initialize() {
        lblNomeDesenvolvedor.setText("Alan Martins e Anderson Frasseti");

        configurarTransicaoAutomatica();
    }

    /**
     * Método chamado ao clicar no botão "Continuar".
     * Fecha a splash screen e abre a tela de login.
     */
    @FXML
    public void onContinuar() {
        try {
            // Carrega a tela de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufrn/sigeaj/view/login.fxml"));
            Parent root = loader.load();

            // Obtém o Stage atual (janela da splash)
            Stage stage = (Stage) btnContinuar.getScene().getWindow();

            // Cria nova cena com a tela de login
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SIGEAJ - Login");
            stage.centerOnScreen();

            System.out.println("Navegando da Splash Screen para Login...");

        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método configurar transição automática.
     */
    private void configurarTransicaoAutomatica() {
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> onContinuar());
        delay.play();
    }
}
