package br.edu.ufrn.sigeaj.controller;

import br.edu.ufrn.sigeaj.model.SetorProdutivo;
import br.edu.ufrn.sigeaj.service.SetorProdutivoService;
import br.edu.ufrn.sigeaj.util.PermissaoHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller da tela de CRUD de Setores Produtivos.
 * CAMADA VIEW - demonstra separação MVC com Service fazendo validações.
 */
public class SetorProdutivoController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private TextField txtResponsavel;

    @FXML
    private TableView<SetorProdutivo> tblSetores;

    @FXML
    private TableColumn<SetorProdutivo, Long> colId;

    @FXML
    private TableColumn<SetorProdutivo, String> colNome;

    @FXML
    private TableColumn<SetorProdutivo, String> colDescricao;

    @FXML
    private TableColumn<SetorProdutivo, String> colResponsavel;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnLimparBusca;

    @FXML
    private Button btnExcluir;

    private final SetorProdutivoService setorService;
    private final ObservableList<SetorProdutivo> setores;
    private SetorProdutivo setorSelecionado;

    public SetorProdutivoController() {
        this.setorService = new SetorProdutivoService();
        this.setores = FXCollections.observableArrayList();
    }

    /**
     * Inicializa a tela, configurando a tabela e carregando dados.
     * Também configura as permissões baseadas no perfil do usuário.
     */
    @FXML
    public void initialize() {
        configurarTabela();
        carregarSetores();
        configurarSelecaoTabela();
        configurarPermissoes();
    }

    /**
     * Configura as permissões baseadas no perfil do usuário.
     * OPERADOR: não pode excluir registros.
     */
    private void configurarPermissoes() {
        if (!PermissaoHelper.podeExcluir()) {
            btnExcluir.setDisable(true);
            btnExcluir.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: white; -fx-opacity: 0.6;");
            btnExcluir.setTooltip(new Tooltip("Apenas ADMINISTRADORES podem excluir"));
        }
    }

    /**
     * Configura as colunas da tabela.
     */
    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));

        tblSetores.setItems(setores);
    }

    /**
     * Carrega os setores do banco de dados para a tabela.
     */
    private void carregarSetores() {
        try {
            setores.clear();
            setores.addAll(setorService.listarTodos());
        } catch (Exception e) {
            exibirErro("Erro ao carregar setores", e.getMessage());
        }
    }

    /**
     * Configura o evento de seleção na tabela.
     */
    private void configurarSelecaoTabela() {
        tblSetores.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setorSelecionado = newValue;
                    preencherCampos(newValue);
                }
            }
        );
    }

    /**
     * Preenche os campos do formulário com dados do setor selecionado.
     */
    private void preencherCampos(SetorProdutivo setor) {
        txtNome.setText(setor.getNome());
        txtDescricao.setText(setor.getDescricao());
        txtResponsavel.setText(setor.getResponsavel());
    }

    /**
     * Limpa os campos do formulário.
     */
    private void limparCampos() {
        txtNome.clear();
        txtDescricao.clear();
        txtResponsavel.clear();
        setorSelecionado = null;
        tblSetores.getSelectionModel().clearSelection();
    }

    /**
     * Botão Novo - limpa o formulário para novo cadastro.
     */
    @FXML
    public void onNovo() {
        limparCampos();
        txtNome.requestFocus();
    }

    /**
     * Botão Salvar - salva ou atualiza um setor.
     * A LÓGICA DE VALIDAÇÃO está no SERVICE, não no Controller!
     */
    @FXML
    public void onSalvar() {
        try {
            SetorProdutivo setor;

            if (setorSelecionado == null) {
                // Modo inserção
                setor = new SetorProdutivo();
            } else {
                // Modo edição
                setor = setorSelecionado;
            }

            // Preenche o objeto com dados do formulário
            setor.setNome(txtNome.getText());
            setor.setDescricao(txtDescricao.getText());
            setor.setResponsavel(txtResponsavel.getText());

            // Chama o SERVICE para validar e salvar
            if (setorSelecionado == null) {
                setorService.cadastrar(setor);
                exibirSucesso("Sucesso", "Setor cadastrado com sucesso!");
            } else {
                setorService.atualizar(setor);
                exibirSucesso("Sucesso", "Setor atualizado com sucesso!");
            }

            limparCampos();
            carregarSetores();

        } catch (Exception e) {
            exibirErro("Erro ao salvar", e.getMessage());
        }
    }

    /**
     * Botão Excluir - remove um setor selecionado.
     * CONTROLE DE ACESSO: Apenas ADMIN pode excluir.
     */
    @FXML
    public void onExcluir() {
        if (setorSelecionado == null) {
            exibirAviso("Atenção", "Selecione um setor para excluir");
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o setor?");
        confirmacao.setContentText("Esta ação também excluirá todas as atividades e registros relacionados!");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                setorService.remover(setorSelecionado.getId());
                exibirSucesso("Sucesso", "Setor excluído com sucesso!");
                limparCampos();
                carregarSetores();
            } catch (Exception e) {
                exibirErro("Erro ao excluir", e.getMessage());
            }
        }
    }

    /**
     * Botão Cancelar - limpa o formulário.
     */
    @FXML
    public void onCancelar() {
        limparCampos();
    }

    /**
     * FILTRO: Busca setores por nome.
     * Atende requisito 4.b - filtros de consulta.
     */
    @FXML
    public void onBuscar() {
        try {
            String termoBusca = txtBuscar.getText();
            setores.clear();

            if (termoBusca == null || termoBusca.trim().isEmpty()) {
                // Se busca vazia, lista todos
                setores.addAll(setorService.listarTodos());
            } else {
                // Busca filtrada
                setores.addAll(setorService.buscarPorNome(termoBusca));
            }

        } catch (Exception e) {
            exibirErro("Erro ao buscar", e.getMessage());
        }
    }

    /**
     * Limpa o filtro de busca e recarrega todos os setores.
     */
    @FXML
    public void onLimparBusca() {
        txtBuscar.clear();
        carregarSetores();
    }

    // Métodos utilitários para alertas

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
