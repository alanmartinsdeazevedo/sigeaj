package br.edu.ufrn.sigeaj.controller;

import br.edu.ufrn.sigeaj.model.Atividade;
import br.edu.ufrn.sigeaj.model.SetorProdutivo;
import br.edu.ufrn.sigeaj.service.AtividadeService;
import br.edu.ufrn.sigeaj.service.SetorProdutivoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

/**
 * Controller da tela de CRUD de Atividades.
 * CAMADA VIEW - demonstra uso de ComboBox para relacionamento com SetorProdutivo.
 */
public class AtividadeController {

    @FXML
    private ComboBox<SetorProdutivo> cmbSetor;

    @FXML
    private TextField txtTipo;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private DatePicker dtpDataExecucao;

    @FXML
    private TableView<Atividade> tblAtividades;

    @FXML
    private TableColumn<Atividade, Long> colId;

    @FXML
    private TableColumn<Atividade, String> colSetor;

    @FXML
    private TableColumn<Atividade, String> colTipo;

    @FXML
    private TableColumn<Atividade, String> colDescricao;

    @FXML
    private TableColumn<Atividade, LocalDate> colDataExecucao;

    private final AtividadeService atividadeService;
    private final SetorProdutivoService setorService;
    private final ObservableList<Atividade> atividades;
    private Atividade atividadeSelecionada;

    public AtividadeController() {
        this.atividadeService = new AtividadeService();
        this.setorService = new SetorProdutivoService();
        this.atividades = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarSetores();
        carregarAtividades();
        configurarSelecaoTabela();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSetor.setCellValueFactory(new PropertyValueFactory<>("setorNome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDataExecucao.setCellValueFactory(new PropertyValueFactory<>("dataExecucao"));

        tblAtividades.setItems(atividades);
    }

    /**
     * Carrega os setores para o ComboBox.
     * Demonstra ASSOCIAÇÃO entre entidades.
     */
    private void carregarSetores() {
        try {
            ObservableList<SetorProdutivo> setores = FXCollections.observableArrayList(
                setorService.listarTodos()
            );
            cmbSetor.setItems(setores);
        } catch (Exception e) {
            exibirErro("Erro ao carregar setores", e.getMessage());
        }
    }

    private void carregarAtividades() {
        try {
            atividades.clear();
            atividades.addAll(atividadeService.listarTodos());
        } catch (Exception e) {
            exibirErro("Erro ao carregar atividades", e.getMessage());
        }
    }

    private void configurarSelecaoTabela() {
        tblAtividades.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    atividadeSelecionada = newValue;
                    preencherCampos(newValue);
                }
            }
        );
    }

    private void preencherCampos(Atividade atividade) {
        // Seleciona o setor no ComboBox
        for (SetorProdutivo setor : cmbSetor.getItems()) {
            if (setor.getId().equals(atividade.getSetorId())) {
                cmbSetor.setValue(setor);
                break;
            }
        }

        txtTipo.setText(atividade.getTipo());
        txtDescricao.setText(atividade.getDescricao());
        dtpDataExecucao.setValue(atividade.getDataExecucao());
    }

    private void limparCampos() {
        cmbSetor.setValue(null);
        txtTipo.clear();
        txtDescricao.clear();
        dtpDataExecucao.setValue(null);
        atividadeSelecionada = null;
        tblAtividades.getSelectionModel().clearSelection();
    }

    @FXML
    public void onNovo() {
        limparCampos();
        cmbSetor.requestFocus();
    }

    @FXML
    public void onSalvar() {
        try {
            Atividade atividade;

            if (atividadeSelecionada == null) {
                atividade = new Atividade();
            } else {
                atividade = atividadeSelecionada;
            }

            // Valida se setor foi selecionado
            if (cmbSetor.getValue() == null) {
                exibirAviso("Atenção", "Selecione um setor");
                return;
            }

            atividade.setSetorId(cmbSetor.getValue().getId());
            atividade.setTipo(txtTipo.getText());
            atividade.setDescricao(txtDescricao.getText());
            atividade.setDataExecucao(dtpDataExecucao.getValue());

            // SERVICE faz as validações
            if (atividadeSelecionada == null) {
                atividadeService.cadastrar(atividade);
                exibirSucesso("Sucesso", "Atividade cadastrada com sucesso!");
            } else {
                atividadeService.atualizar(atividade);
                exibirSucesso("Sucesso", "Atividade atualizada com sucesso!");
            }

            limparCampos();
            carregarAtividades();

        } catch (Exception e) {
            exibirErro("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    public void onExcluir() {
        if (atividadeSelecionada == null) {
            exibirAviso("Atenção", "Selecione uma atividade para excluir");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir a atividade?");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                atividadeService.remover(atividadeSelecionada.getId());
                exibirSucesso("Sucesso", "Atividade excluída com sucesso!");
                limparCampos();
                carregarAtividades();
            } catch (Exception e) {
                exibirErro("Erro ao excluir", e.getMessage());
            }
        }
    }

    @FXML
    public void onCancelar() {
        limparCampos();
    }

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

    private void exibirAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
