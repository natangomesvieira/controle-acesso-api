package br.com.controleacesso.service;

import br.com.controleacesso.dao.UsuarioDAO;
import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;

public class CadastroService {
    
    private final UsuarioDAO dao;
    
    public CadastroService(UsuarioDAO dao) {
        this.dao = dao;
    }
    
    public void criarUsuario(Usuario usuario) {
        if(this.validarSenha(usuario.getSenha(), usuario.getConfSenha())) {
            try {
                dao.salvar(usuario);
            } catch (SQLException e) {
                
            }
        }
    }
    
    private boolean validarSenha(String senha, String confSenha) {
        return senha.equals(confSenha);
    }
    
}
