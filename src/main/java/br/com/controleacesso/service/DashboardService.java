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
    
    public void autorizarAcessoByEmail(String email) throws SQLException {
        repository.autorizarAcessoByEmail(email);
    }
    
    public void rejeitarAcessoByEmail(String email) throws SQLException {
        repository.rejeitarAcessoByEmail(email);
    }
    
}
