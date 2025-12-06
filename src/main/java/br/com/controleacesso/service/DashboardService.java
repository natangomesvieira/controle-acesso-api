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
    
    public List<Usuario> getAllUsuarios () {
        try {
            return repository.getAllUsuarios();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao verificar usuários: " + ex.getMessage(), ex);
        }
    }
    
}
