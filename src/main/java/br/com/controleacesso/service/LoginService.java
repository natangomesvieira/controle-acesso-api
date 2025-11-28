package br.com.controleacesso.service;

import br.com.controleacesso.dao.UsuarioDAO;
import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;

public class LoginService {
    
    private final UsuarioDAO dao;
    
    public LoginService(UsuarioDAO dao) {
        this.dao = dao;
    }
    
    public String login(Usuario usuario) {
        try {
            Usuario usuarioExiste = dao.getByEmail(usuario.getEmail());
            if(usuarioExiste != null) {
                if(usuario.getSenha().equals(usuarioExiste.getSenha())) {
                    return "Login realizado com sucesso!";
                } else {
                    return "Usuário ou senha inválido!";
                }
            }
       } catch (SQLException e) {
           //log de erro
       }
        return null;
    }
    
}
