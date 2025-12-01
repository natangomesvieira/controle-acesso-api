package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;

public class LoginService {
    
    private final UsuarioRepository repository;
    
    public LoginService(UsuarioRepository repository) {
        this.repository = repository;
    }
    
    public String login(Usuario usuario) {
        try {
            Usuario usuarioExiste = repository.getByEmail(usuario.getEmail());
            if(usuarioExiste != null) {
                if(usuario.getSenha().equals(usuarioExiste.getSenha())) {
                    return "Login realizado com sucesso!";
                } else {
                    return "Usuário ou senha inválido!";
                }
            } else {
                return "Usuário não encontrado!";
            }
       } catch (SQLException e) {
           throw new RuntimeException("Erro ao consultar no banco de dados: " + e.getMessage(), e);
       }
    }
    
}
