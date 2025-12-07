package br.com.controleacesso.service;

import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.UsuarioRepository;
import java.sql.SQLException;
import java.util.List;

public class DashboardService {
    
    private final UsuarioRepository repository;
    
    public DashboardService(UsuarioRepository repository) {
        this.repository = repository;
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
    
}
