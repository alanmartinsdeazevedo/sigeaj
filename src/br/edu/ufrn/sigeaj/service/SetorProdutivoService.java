package br.edu.ufrn.sigeaj.service;

import br.edu.ufrn.sigeaj.dao.SetorProdutivoDAO;
import br.edu.ufrn.sigeaj.model.SetorProdutivo;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe de serviço para SetorProdutivo.
 * CAMADA DE NEGÓCIO - validações e regras de negócio para setores produtivos.
 */
public class SetorProdutivoService {

    private final SetorProdutivoDAO setorDAO;

    public SetorProdutivoService() {
        this.setorDAO = new SetorProdutivoDAO();
    }

    /**
     * Cadastra um novo setor produtivo.
     * REGRAS DE NEGÓCIO:
     * - Nome é obrigatório e deve ser único
     * - Descrição é obrigatória
     */
    public void cadastrar(SetorProdutivo setor) throws Exception {
        validarCamposObrigatorios(setor);

        try {
            // Verifica se já existe setor com mesmo nome
            if (setorDAO.existeNome(setor.getNome())) {
                throw new Exception("Já existe um setor com este nome");
            }

            if (!setorDAO.inserir(setor)) {
                throw new Exception("Erro ao cadastrar setor");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar setor: " + e.getMessage());
        }
    }

    /**
     * Atualiza um setor produtivo existente.
     */
    public void atualizar(SetorProdutivo setor) throws Exception {
        if (setor.getId() == null) {
            throw new Exception("ID do setor é obrigatório para atualização");
        }

        validarCamposObrigatorios(setor);

        try {
            // Verifica se já existe outro setor com mesmo nome
            if (setorDAO.existeNomeExceto(setor.getNome(), setor.getId())) {
                throw new Exception("Já existe outro setor com este nome");
            }

            if (!setorDAO.atualizar(setor)) {
                throw new Exception("Setor não encontrado");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar setor: " + e.getMessage());
        }
    }

    /**
     * Remove um setor produtivo.
     * ATENÇÃO: Remove também todas as atividades e registros relacionados (CASCADE).
     */
    public void remover(Long id) throws Exception {
        if (id == null) {
            throw new Exception("ID do setor é obrigatório");
        }

        try {
            if (!setorDAO.remover(id)) {
                throw new Exception("Setor não encontrado");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover setor: " + e.getMessage());
        }
    }

    /**
     * Busca setor por ID.
     */
    public SetorProdutivo buscarPorId(Long id) throws Exception {
        try {
            return setorDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar setor: " + e.getMessage());
        }
    }

    /**
     * Lista todos os setores.
     */
    public List<SetorProdutivo> listarTodos() throws Exception {
        try {
            return setorDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar setores: " + e.getMessage());
        }
    }

    // Métodos auxiliares de validação

    private void validarCamposObrigatorios(SetorProdutivo setor) throws Exception {
        if (setor.getNome() == null || setor.getNome().trim().isEmpty()) {
            throw new Exception("Nome do setor é obrigatório");
        }

        if (setor.getDescricao() == null || setor.getDescricao().trim().isEmpty()) {
            throw new Exception("Descrição do setor é obrigatória");
        }

        // Responsável é opcional, mas se preenchido não pode ser vazio
        if (setor.getResponsavel() != null && setor.getResponsavel().trim().isEmpty()) {
            setor.setResponsavel(null);
        }
    }
}
