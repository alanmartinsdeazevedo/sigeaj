package br.edu.ufrn.sigeaj.controller;

import br.edu.ufrn.sigeaj.model.Usuario;
import br.edu.ufrn.sigeaj.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller da tela de login.
 * CAMADA VIEW (Controller no padrão MVC) - responsável por gerenciar a interface
 * gráfica e intermediar a comunicação entre a VIEW (FXML) e a lógica de negócio (Service).
 */
public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    private final UsuarioService usuarioService;
    private static Usuario usuarioLogado;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    /**
     * Método chamado automaticamente após o carregamento do FXML.
     * Inicializa componentes da tela se necessário.
     */
    @FXML
    public void initialize() {
        // Configurações iniciais, se necessário
    }

    /**
     * Método acionado ao clicar no botão "Entrar".
     * Valida as credenciais e abre a tela principal se login bem-sucedido.
     */
    @FXML
    public void onEntrar() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        try {
            // Chama o SERVICE para validar o login (lógica de negócio)
            Usuario usuario = usuarioService.login(email, senha);

            // Armazena o usuário logado para uso nas outras telas
            usuarioLogado = usuario;

            // Exibe mensagem de sucesso
            exibirSucesso("Login realizado com sucesso!", "Bem-vindo(a), " + usuario.getNome());

            // Abre a tela principal
            abrirTelaPrincipal();

        } catch (Exception e) {
            // Exibe mensagem de erro caso login falhe
            exibirErro("Erro no login", e.getMessage());
        }
    }

    /**
     * Abre a tela principal do sistema.
     */
    private void abrirTelaPrincipal() {
        try {
            // Carrega o FXML da tela principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufrn/sigeaj/view/main.fxml"));
            Parent root = loader.load();

            // Cria um novo Stage (janela)
            Stage stage = new Stage();
            stage.setTitle("SIGEAJ - Sistema de Gestão de Setores Produtivos");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Fecha a tela de login
            Stage loginStage = (Stage) txtEmail.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            exibirErro("Erro ao abrir tela principal", e.getMessage());
        }
    }

    /**
     * Retorna o usuário atualmente logado no sistema.
     */
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Métodos utilitários para exibir alertas

    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
