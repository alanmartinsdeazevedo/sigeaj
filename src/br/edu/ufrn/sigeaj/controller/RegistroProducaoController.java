package br.edu.ufrn.sigeaj.controller;

import br.edu.ufrn.sigeaj.model.RegistroProducao;
import br.edu.ufrn.sigeaj.model.SetorProdutivo;
import br.edu.ufrn.sigeaj.service.RegistroProducaoService;
import br.edu.ufrn.sigeaj.service.SetorProdutivoService;
import br.edu.ufrn.sigeaj.util.PermissaoHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controller da tela de CRUD de Registros de Produção.
 */
public class RegistroProducaoController {

    @FXML
    private ComboBox<SetorProdutivo> cmbSetor;

    @FXML
    private DatePicker dtpDataRegistro;

    @FXML
    private TextField txtProduto;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtUnidade;

    @FXML
    private TableView<RegistroProducao> tblRegistros;

    @FXML
    private TableColumn<RegistroProducao, Long> colId;

    @FXML
    private TableColumn<RegistroProducao, String> colSetor;

    @FXML
    private TableColumn<RegistroProducao, LocalDate> colData;

    @FXML
    private TableColumn<RegistroProducao, String> colProduto;

    @FXML
    private TableColumn<RegistroProducao, BigDecimal> colQuantidade;

    @FXML
    private TableColumn<RegistroProducao, String> colUnidade;

    @FXML
    private Button btnExcluir;

    private final RegistroProducaoService registroService;
    private final SetorProdutivoService setorService;
    private final ObservableList<RegistroProducao> registros;
    private RegistroProducao registroSelecionado;

    public RegistroProducaoController() {
        this.registroService = new RegistroProducaoService();
        this.setorService = new SetorProdutivoService();
        this.registros = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarSetores();
        carregarRegistros();
        configurarSelecaoTabela();
        configurarPermissoes();
    }

    private void configurarPermissoes() {
        if (!PermissaoHelper.podeExcluir()) {
            btnExcluir.setDisable(true);
            btnExcluir.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: white; -fx-opacity: 0.6;");
            btnExcluir.setTooltip(new Tooltip("Apenas ADMINISTRADORES podem excluir"));
        }
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSetor.setCellValueFactory(new PropertyValueFactory<>("setorNome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataRegistro"));
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));

        tblRegistros.setItems(registros);
    }

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

    private void carregarRegistros() {
        try {
            registros.clear();
            registros.addAll(registroService.listarTodos());
        } catch (Exception e) {
            exibirErro("Erro ao carregar registros", e.getMessage());
        }
    }

    private void configurarSelecaoTabela() {
        tblRegistros.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    registroSelecionado = newValue;
                    preencherCampos(newValue);
                }
            }
        );
    }

    private void preencherCampos(RegistroProducao registro) {
        // Seleciona o setor
        for (SetorProdutivo setor : cmbSetor.getItems()) {
            if (setor.getId().equals(registro.getSetorId())) {
                cmbSetor.setValue(setor);
                break;
            }
        }

        dtpDataRegistro.setValue(registro.getDataRegistro());
        txtProduto.setText(registro.getProduto());
        txtQuantidade.setText(registro.getQuantidade().toString());
        txtUnidade.setText(registro.getUnidade());
    }

    private void limparCampos() {
        cmbSetor.setValue(null);
        dtpDataRegistro.setValue(null);
        txtProduto.clear();
        txtQuantidade.clear();
        txtUnidade.clear();
        registroSelecionado = null;
        tblRegistros.getSelectionModel().clearSelection();
    }

    @FXML
    public void onNovo() {
        limparCampos();
        cmbSetor.requestFocus();
    }

    @FXML
    public void onSalvar() {
        try {
            RegistroProducao registro;

            if (registroSelecionado == null) {
                registro = new RegistroProducao();
            } else {
                registro = registroSelecionado;
            }

            // Valida se setor foi selecionado
            if (cmbSetor.getValue() == null) {
                exibirAviso("Atenção", "Selecione um setor");
                return;
            }

            registro.setSetorId(cmbSetor.getValue().getId());
            registro.setDataRegistro(dtpDataRegistro.getValue());
            registro.setProduto(txtProduto.getText());

            // Converte quantidade para BigDecimal
            try {
                registro.setQuantidade(new BigDecimal(txtQuantidade.getText()));
            } catch (NumberFormatException e) {
                exibirErro("Erro", "Quantidade inválida. Use apenas números.");
                return;
            }

            registro.setUnidade(txtUnidade.getText());

            // SERVICE faz as validações de negócio
            if (registroSelecionado == null) {
                registroService.cadastrar(registro);
                exibirSucesso("Sucesso", "Registro cadastrado com sucesso!");
            } else {
                registroService.atualizar(registro);
                exibirSucesso("Sucesso", "Registro atualizado com sucesso!");
            }

            limparCampos();
            carregarRegistros();

        } catch (Exception e) {
            exibirErro("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    public void onExcluir() {
        if (registroSelecionado == null) {
            exibirAviso("Atenção", "Selecione um registro para excluir");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o registro?");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                registroService.remover(registroSelecionado.getId());
                exibirSucesso("Sucesso", "Registro excluído com sucesso!");
                limparCampos();
                carregarRegistros();
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
