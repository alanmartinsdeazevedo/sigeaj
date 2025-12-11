package br.edu.ufrn.sigeaj.service;

import br.edu.ufrn.sigeaj.dao.AtividadeDAO;
import br.edu.ufrn.sigeaj.dao.SetorProdutivoDAO;
import br.edu.ufrn.sigeaj.model.Atividade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe de serviço para Atividade. - validações e regras de negócio
 */
public class AtividadeService {

    private final AtividadeDAO atividadeDAO;
    private final SetorProdutivoDAO setorDAO;

    public AtividadeService() {
        this.atividadeDAO = new AtividadeDAO();
        this.setorDAO = new SetorProdutivoDAO();
    }

    /**
     * Cadastra uma nova atividade.
     * REGRAS DE NEGÓCIO:
     * - Todos os campos obrigatórios devem ser preenchidos
     * - Setor deve existir no banco
     * - Data de execução não pode ser nula
     * - Data de execução não pode ser muito antiga (mais de 5 anos)
     */
    public void cadastrar(Atividade atividade) throws Exception {
        validarCamposObrigatorios(atividade);
        validarDataExecucao(atividade.getDataExecucao());
        validarSetor(atividade.getSetorId());

        try {
            if (!atividadeDAO.inserir(atividade)) {
                throw new Exception("Erro ao cadastrar atividade");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar atividade: " + e.getMessage());
        }
    }

    /**
     * Atualiza uma atividade existente.
     */
    public void atualizar(Atividade atividade) throws Exception {
        if (atividade.getId() == null) {
            throw new Exception("ID da atividade é obrigatório para atualização");
        }

        validarCamposObrigatorios(atividade);
        validarDataExecucao(atividade.getDataExecucao());
        validarSetor(atividade.getSetorId());

        try {
            if (!atividadeDAO.atualizar(atividade)) {
                throw new Exception("Atividade não encontrada");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar atividade: " + e.getMessage());
        }
    }

    /**
     * Remove uma atividade.
     */
    public void remover(Long id) throws Exception {
        if (id == null) {
            throw new Exception("ID da atividade é obrigatório");
        }

        try {
            if (!atividadeDAO.remover(id)) {
                throw new Exception("Atividade não encontrada");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover atividade: " + e.getMessage());
        }
    }

    /**
     * Busca atividade por ID.
     */
    public Atividade buscarPorId(Long id) throws Exception {
        try {
            return atividadeDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar atividade: " + e.getMessage());
        }
    }

    /**
     * Lista todas as atividades.
     */
    public List<Atividade> listarTodos() throws Exception {
        try {
            return atividadeDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar atividades: " + e.getMessage());
        }
    }

    /**
     * Lista atividades de um setor específico.
     */
    public List<Atividade> listarPorSetor(Long setorId) throws Exception {
        try {
            return atividadeDAO.listarPorSetor(setorId);
        } catch (SQLException e) {
            throw new Exception("Erro ao listar atividades do setor: " + e.getMessage());
        }
    }

    /**
     * 4.b - Busca atividades por período de data.
     */
    public List<Atividade> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        if (dataInicio == null || dataFim == null) {
            throw new Exception("Data inicial e final são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new Exception("Data inicial não pode ser posterior à data final");
        }

        try {
            return atividadeDAO.buscarPorPeriodo(dataInicio, dataFim);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar atividades por período: " + e.getMessage());
        }
    }

    /**
     * 4.b - Busca atividades por tipo.
     */
    public List<Atividade> buscarPorTipo(String tipo) throws Exception {
        if (tipo == null || tipo.trim().isEmpty()) {
            return listarTodos();
        }

        try {
            return atividadeDAO.buscarPorTipo(tipo.trim());
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar atividades por tipo: " + e.getMessage());
        }
    }

    // Métodos auxiliares de validação

    private void validarCamposObrigatorios(Atividade atividade) throws Exception {
        if (atividade.getSetorId() == null) {
            throw new Exception("Setor é obrigatório");
        }

        if (atividade.getTipo() == null || atividade.getTipo().trim().isEmpty()) {
            throw new Exception("Tipo da atividade é obrigatório");
        }

        if (atividade.getDescricao() == null || atividade.getDescricao().trim().isEmpty()) {
            throw new Exception("Descrição da atividade é obrigatória");
        }

        if (atividade.getDataExecucao() == null) {
            throw new Exception("Data de execução é obrigatória");
        }
    }

    /**
     * REGRA DE NEGÓCIO: Data de execução não pode ser muito antiga (mais de 5 anos).
     */
    private void validarDataExecucao(LocalDate data) throws Exception {
        if (data == null) {
            throw new Exception("Data de execução é obrigatória");
        }

        LocalDate dataLimite = LocalDate.now().minusYears(5);
        if (data.isBefore(dataLimite)) {
            throw new Exception("Data de execução não pode ser anterior a " + dataLimite);
        }

        // limitar datas futuras
        LocalDate dataFuturaLimite = LocalDate.now().plusMonths(1);
        if (data.isAfter(dataFuturaLimite)) {
            throw new Exception("Data de execução não pode ser superior a " + dataFuturaLimite);
        }
    }

    /**
     * REGRA DE NEGÓCIO: Valida se o setor existe no banco de dados.
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
