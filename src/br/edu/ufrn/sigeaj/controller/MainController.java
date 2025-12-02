package br.edu.ufrn.sigeaj.controller;

import br.edu.ufrn.sigeaj.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller da tela principal (menu).
 * CAMADA VIEW - gerencia a navegação entre as diferentes telas do sistema.
 */
public class MainController {

    @FXML
    private Label lblUsuarioLogado;

    /**
     * Inicializa a tela principal, exibindo informações do usuário logado.
     */
    @FXML
    public void initialize() {
        Usuario usuario = LoginController.getUsuarioLogado();
        if (usuario != null) {
            lblUsuarioLogado.setText("Usuário: " + usuario.getNome() + " (" + usuario.getPerfil().getDescricao() + ")");
        }
    }

    /**
     * Abre a tela de gerenciamento de setores produtivos.
     */
    @FXML
    public void onGerenciarSetores() {
        abrirTela("Gerenciar Setores Produtivos", "/br/edu/ufrn/sigeaj/view/setor_produtivo.fxml");
    }

    /**
     * Abre a tela de gerenciamento de atividades.
     */
    @FXML
    public void onGerenciarAtividades() {
        abrirTela("Gerenciar Atividades", "/br/edu/ufrn/sigeaj/view/atividade.fxml");
    }

    /**
     * Abre a tela de gerenciamento de registros de produção.
     */
    @FXML
    public void onGerenciarRegistros() {
        abrirTela("Gerenciar Registros de Produção", "/br/edu/ufrn/sigeaj/view/registro_producao.fxml");
    }

    /**
     * Realiza o logout e volta para a tela de login.
     */
    @FXML
    public void onSair() {
        try {
            // Carrega a tela de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufrn/sigeaj/view/login.fxml"));
            Parent root = loader.load();

            // Cria novo Stage
            Stage stage = new Stage();
            stage.setTitle("SIGEAJ - Login");
            stage.setScene(new Scene(root));
            stage.show();

            // Fecha a tela principal
            Stage mainStage = (Stage) lblUsuarioLogado.getScene().getWindow();
            mainStage.close();

        } catch (Exception e) {
            exibirErro("Erro ao sair", e.getMessage());
        }
    }

    /**
     * Método auxiliar para abrir telas em novas janelas.
     */
    private void abrirTela(String titulo, String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("SIGEAJ - " + titulo);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            exibirErro("Erro ao abrir tela", e.getMessage());
        }
    }

    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
