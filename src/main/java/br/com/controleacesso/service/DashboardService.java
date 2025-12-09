package br.com.controleacesso.service;

import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.UsuarioRepository;
import com.pss.senha.validacao.ValidadorSenha;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DashboardService {
    
    private final UsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    
    public DashboardService(UsuarioRepository repository, ValidadorSenha validadorSenha) {
        this.repository = repository;
        this.validadorSenha = validadorSenha;
    }
    
    public List<Usuario> getAllUsuariosNaoAutorizados() throws SQLException {
        return repository.getAllUsuariosNaoAutorizados();
    }
    
    public List<Usuario> getAllUsuarios() throws SQLException {
        return repository.getAllUsuarios();
    }
    
    public void autorizarAcessoByEmail(String email) throws SQLException {
        repository.autorizarAcessoByEmail(email);
    }
    
    public void rejeitarAcessoByEmail(String email) throws SQLException {
        repository.rejeitarAcessoByEmail(email);
    }
    
    public void promoverUsuario(String email, String perfil) throws SQLException {
        if("administrador".equals(perfil)) {
            throw new IllegalArgumentException("O usuário selecionado já é um administrador.");
        }
        repository.alterarPefilUsuarioByEmail(email, "administrador");
    }
    
    public void rebaixarUsuario(String email, String perfil) throws SQLException {
        if("usuario_padrao".equals(perfil)) {
            throw new IllegalArgumentException("O usuário selecionado já é um usuário padrão.");
        }
        repository.alterarPefilUsuarioByEmail(email, "usuario_padrao");
    }
    
    public void excluirUsuario(int id) throws SQLException {
        if (id == 1) {
            throw new IllegalArgumentException("O Administrador Principal (Root) não pode ser excluído.");
        }
        repository.deletarUsuario(id);
    }
    
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
    
}
