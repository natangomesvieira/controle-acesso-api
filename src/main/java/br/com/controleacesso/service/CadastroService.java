package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;

public class CadastroService {
    
    private final UsuarioRepository repository;
    
    public CadastroService(UsuarioRepository repository) {
        this.repository = repository;
    }
    
    public void criarUsuario(Usuario usuario) {
        try {
            validarSenha(usuario.getSenha(), usuario.getConfSenha());
            repository.salvar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar no banco de dados: " + e.getMessage(), e);
        }
    }
    
    private void validarSenha(String senha, String confSenha) {
        if(!senha.equals(confSenha)) {
            throw new IllegalArgumentException("As senhas não são iguais!");
        }
    }
    
}
