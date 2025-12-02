package br.edu.ufrn.sigeaj.service;

import br.edu.ufrn.sigeaj.dao.UsuarioDAO;
import br.edu.ufrn.sigeaj.model.Usuario;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe de serviço para Usuario.
 * CAMADA DE NEGÓCIO - contém as regras de negócio e validações.
 * Faz a mediação entre os Controllers (VIEW) e os DAOs (PERSISTÊNCIA).
 * Implementa o padrão MVC separando a lógica de negócio da apresentação.
 */
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Realiza o login do usuário validando email e senha.
     * REGRA DE NEGÓCIO: Autentica credenciais no banco de dados.
     *
     * @param email email do usuário
     * @param senha senha do usuário
     * @return Usuario se login bem-sucedido, null se credenciais inválidas
     * @throws Exception se houver erro na validação ou banco de dados
     */
    public Usuario login(String email, String senha) throws Exception {
        // Validações de entrada
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email é obrigatório");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new Exception("Senha é obrigatória");
        }

        // Validação de formato de email
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new Exception("Email em formato inválido");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmailESenha(email, senha);

            if (usuario == null) {
                throw new Exception("Email ou senha inválidos");
            }

            return usuario;
        } catch (SQLException e) {
            throw new Exception("Erro ao realizar login: " + e.getMessage());
        }
    }

    /**
     * Cadastra um novo usuário.
     * REGRAS DE NEGÓCIO:
     * - Todos os campos obrigatórios devem ser preenchidos
     * - Email deve ser único no sistema
     * - Email deve ter formato válido
     * - Senha deve ter no mínimo 6 caracteres
     */
    public void cadastrar(Usuario usuario) throws Exception {
        // Validações
        validarCamposObrigatorios(usuario);
        validarEmail(usuario.getEmail());
        validarSenha(usuario.getSenha());

        try {
            // Verifica se email já existe
            if (usuarioDAO.existeEmail(usuario.getEmail())) {
                throw new Exception("Email já cadastrado no sistema");
            }

            // Insere no banco
            if (!usuarioDAO.inserir(usuario)) {
                throw new Exception("Erro ao cadastrar usuário");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    /**
     * Atualiza um usuário existente.
     */
    public void atualizar(Usuario usuario) throws Exception {
        if (usuario.getId() == null) {
            throw new Exception("ID do usuário é obrigatório para atualização");
        }

        validarCamposObrigatorios(usuario);
        validarEmail(usuario.getEmail());
        validarSenha(usuario.getSenha());

        try {
            if (!usuarioDAO.atualizar(usuario)) {
                throw new Exception("Usuário não encontrado");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    /**
     * Remove um usuário.
     */
    public void remover(Long id) throws Exception {
        if (id == null) {
            throw new Exception("ID do usuário é obrigatório");
        }

        try {
            if (!usuarioDAO.remover(id)) {
                throw new Exception("Usuário não encontrado");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover usuário: " + e.getMessage());
        }
    }

    /**
     * Busca usuário por ID.
     */
    public Usuario buscarPorId(Long id) throws Exception {
        try {
            return usuarioDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    /**
     * Lista todos os usuários.
     */
    public List<Usuario> listarTodos() throws Exception {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar usuários: " + e.getMessage());
        }
    }

    // Métodos auxiliares de validação

    private void validarCamposObrigatorios(Usuario usuario) throws Exception {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new Exception("Email é obrigatório");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new Exception("Senha é obrigatória");
        }

        if (usuario.getPerfil() == null) {
            throw new Exception("Perfil é obrigatório");
        }
    }

    private void validarEmail(String email) throws Exception {
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new Exception("Email em formato inválido");
        }
    }

    private void validarSenha(String senha) throws Exception {
        if (senha.length() < 6) {
            throw new Exception("Senha deve ter no mínimo 6 caracteres");
        }
    }
}
