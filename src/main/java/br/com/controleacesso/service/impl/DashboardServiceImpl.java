package br.com.controleacesso.service.impl;

import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.service.IDashboardService;
import com.pss.senha.validacao.ValidadorSenha;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DashboardServiceImpl implements IDashboardService {
    
    private final IUsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    
    public DashboardServiceImpl(IUsuarioRepository repository, ValidadorSenha validadorSenha) {
        this.repository = repository;
        this.validadorSenha = validadorSenha;
    }
    
    @Override
    public List<Usuario> getAllUsuariosNaoAutorizados() throws SQLException {
        return repository.getAllUsuariosNaoAutorizados();
    }
    
    @Override
    public List<Usuario> getAllUsuarios() throws SQLException {
        return repository.getAllUsuarios();
    }
    
    @Override
    public Usuario getUsuarioById(int id) throws SQLException {
        return repository.getById(id);
    }
    
    @Override
    public void autorizarAcessoByEmail(String email) throws SQLException {
        repository.autorizarAcessoByEmail(email);
    }
    
    @Override
    public void rejeitarAcessoByEmail(String email) throws SQLException {
        repository.rejeitarAcessoByEmail(email);
    }
    
    @Override
    public void promoverUsuario(String email, String perfil, int idSolicitante) throws SQLException, IllegalAccessException {
        if("administrador".equals(perfil)) {
            throw new IllegalArgumentException("O usuário selecionado já é um administrador.");
        }
        
        if (idSolicitante != 1) {
            throw new IllegalAccessException("Permissão negada: Apenas o Administrador Principal pode promover usuários.");
        }
        
        repository.alterarPefilUsuarioByEmail(email, "administrador");
    }
    
    @Override
    public void rebaixarUsuario(String email, String perfil, int idSolicitante) throws SQLException, IllegalAccessException {
        if("usuario_padrao".equals(perfil)) {
            throw new IllegalArgumentException("O usuário selecionado já é um usuário padrão.");
        }
        
        if (idSolicitante != 1) {
            throw new IllegalAccessException("Permissão negada: Apenas o Administrador Principal pode rebaixar usuários.");
        }
        
        repository.alterarPefilUsuarioByEmail(email, "usuario_padrao");
    }
    
    @Override
    public void excluirUsuario(int idAlvo, int idSolicitante) throws SQLException {
        if (idAlvo == 1) {
            throw new IllegalArgumentException("O Administrador Principal (Root) não pode ser excluído.");
        }
        if (idAlvo == idSolicitante) {
            throw new IllegalArgumentException("Segurança: Você não pode excluir sua própria conta.");
        }
        
        repository.deletarUsuario(idAlvo);
    }
    
    @Override
    public void alterarSenha(int idUsuario, String senhaAtual, String novaSenha, String confSenha) throws Exception {
        
        Optional.ofNullable(senhaAtual).filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("A senha atual deve ser informada."));
            
        Optional.ofNullable(novaSenha).filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("A nova senha deve ser informada."));
        
        if (!novaSenha.equals(confSenha)) {
            throw new IllegalArgumentException("A confirmação da nova senha não confere.");
        }
        
        List<String> erros = validadorSenha.validar(novaSenha);
        if (erros != null && !erros.isEmpty()) {
            throw new IllegalArgumentException("Nova senha fraca:\n- " + String.join("\n- ", erros));
        }

        Usuario usuarioBanco = repository.getById(idUsuario);
        if (usuarioBanco == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        
        if (!usuarioBanco.getSenha().equals(senhaAtual)) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        repository.atualizarSenha(idUsuario, novaSenha);
    }
    
    @Override
    public void restaurarSistema(int idAdmin, String senhaDigitada, String confSenhaDigitada) throws Exception {
        
        if (idAdmin != 1) {
            throw new IllegalAccessException("Apenas o primeiro administrador pode realizar esta operação.");
        }
        
        if (!senhaDigitada.equals(confSenhaDigitada)) {
            throw new IllegalArgumentException("As senhas de confirmação não coincidem.");
        }
        
        Usuario admin = repository.getById(idAdmin);
        if (admin == null || !admin.getSenha().equals(senhaDigitada)) {
            throw new IllegalArgumentException("Senha incorreta. A restauração foi cancelada.");
        }
        
        repository.resetarSistemaCompleto();
    }
    
}
