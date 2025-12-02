package br.edu.ufrn.sigeaj.service;

import br.edu.ufrn.sigeaj.dao.RegistroProducaoDAO;
import br.edu.ufrn.sigeaj.dao.SetorProdutivoDAO;
import br.edu.ufrn.sigeaj.model.RegistroProducao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe de serviço para RegistroProducao.
 * CAMADA DE NEGÓCIO - validações e regras de negócio para registros de produção.
 */
public class RegistroProducaoService {

    private final RegistroProducaoDAO registroDAO;
    private final SetorProdutivoDAO setorDAO;

    public RegistroProducaoService() {
        this.registroDAO = new RegistroProducaoDAO();
        this.setorDAO = new SetorProdutivoDAO();
    }

    /**
     * Cadastra um novo registro de produção.
     * REGRAS DE NEGÓCIO:
     * - Todos os campos obrigatórios devem ser preenchidos
     * - Setor deve existir
     * - Quantidade deve ser maior que zero
     * - Data não pode ser futura
     */
    public void cadastrar(RegistroProducao registro) throws Exception {
        validarCamposObrigatorios(registro);
        validarQuantidade(registro.getQuantidade());
        validarDataRegistro(registro.getDataRegistro());
        validarSetor(registro.getSetorId());

        try {
            if (!registroDAO.inserir(registro)) {
                throw new Exception("Erro ao cadastrar registro de produção");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar registro: " + e.getMessage());
        }
    }

    /**
     * Atualiza um registro de produção existente.
     */
    public void atualizar(RegistroProducao registro) throws Exception {
        if (registro.getId() == null) {
            throw new Exception("ID do registro é obrigatório para atualização");
        }

        validarCamposObrigatorios(registro);
        validarQuantidade(registro.getQuantidade());
        validarDataRegistro(registro.getDataRegistro());
        validarSetor(registro.getSetorId());

        try {
            if (!registroDAO.atualizar(registro)) {
                throw new Exception("Registro não encontrado");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar registro: " + e.getMessage());
        }
    }

    /**
     * Remove um registro de produção.
     */
    public void remover(Long id) throws Exception {
        if (id == null) {
            throw new Exception("ID do registro é obrigatório");
        }

        try {
            if (!registroDAO.remover(id)) {
                throw new Exception("Registro não encontrado");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover registro: " + e.getMessage());
        }
    }

    /**
     * Busca registro por ID.
     */
    public RegistroProducao buscarPorId(Long id) throws Exception {
        try {
            return registroDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar registro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os registros.
     */
    public List<RegistroProducao> listarTodos() throws Exception {
        try {
            return registroDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar registros: " + e.getMessage());
        }
    }

    /**
     * Lista registros de um setor específico.
     */
    public List<RegistroProducao> listarPorSetor(Long setorId) throws Exception {
        try {
            return registroDAO.listarPorSetor(setorId);
        } catch (SQLException e) {
            throw new Exception("Erro ao listar registros do setor: " + e.getMessage());
        }
    }

    // Métodos auxiliares de validação

    private void validarCamposObrigatorios(RegistroProducao registro) throws Exception {
        if (registro.getSetorId() == null) {
            throw new Exception("Setor é obrigatório");
        }

        if (registro.getProduto() == null || registro.getProduto().trim().isEmpty()) {
            throw new Exception("Produto é obrigatório");
        }

        if (registro.getQuantidade() == null) {
            throw new Exception("Quantidade é obrigatória");
        }

        if (registro.getUnidade() == null || registro.getUnidade().trim().isEmpty()) {
            throw new Exception("Unidade é obrigatória");
        }

        if (registro.getDataRegistro() == null) {
            throw new Exception("Data de registro é obrigatória");
        }
    }

    /**
     * REGRA DE NEGÓCIO: Quantidade deve ser maior que zero.
     */
    private void validarQuantidade(BigDecimal quantidade) throws Exception {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Quantidade deve ser maior que zero");
        }

        // Validação adicional: quantidade não pode ser absurdamente grande
        if (quantidade.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new Exception("Quantidade excede o limite permitido");
        }
    }

    /**
     * REGRA DE NEGÓCIO: Data de registro não pode ser futura.
     */
    private void validarDataRegistro(LocalDate data) throws Exception {
        if (data == null) {
            throw new Exception("Data de registro é obrigatória");
        }

        if (data.isAfter(LocalDate.now())) {
            throw new Exception("Data de registro não pode ser futura");
        }

        // Opcional: não permitir registros muito antigos
        LocalDate dataLimite = LocalDate.now().minusYears(10);
        if (data.isBefore(dataLimite)) {
            throw new Exception("Data de registro não pode ser anterior a " + dataLimite);
        }
    }

    /**
     * REGRA DE NEGÓCIO: Valida se o setor existe.
     */
    private void validarSetor(Long setorId) throws Exception {
        try {
            if (setorDAO.buscarPorId(setorId) == null) {
                throw new Exception("Setor selecionado não existe");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao validar setor: " + e.getMessage());
        }
    }
}
