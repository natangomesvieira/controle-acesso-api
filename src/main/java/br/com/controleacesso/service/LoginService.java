package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;
import java.nio.file.AccessDeniedException;

public class LoginService {
    
    private final UsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    
    public LoginService(UsuarioRepository repository) {
        this.repository = repository;
        this.validadorSenha = new ValidadorSenha();
    }
    
    public Usuario login(Usuario usuario) throws Exception {
        
        validarDados(usuario);

        Usuario usuarioExiste = repository.getByEmail(usuario.getEmail());
        
        if(usuarioExiste != null) {
            // 1. Verifica Senha
            if(!usuario.getSenha().equals(usuarioExiste.getSenha())) {
                throw new AccessDeniedException("Senha inválida!");
            }

            // 2. Verifica Autorização (US 03)
            if(!usuarioExiste.isAutorizado()) {
                 throw new AccessDeniedException("Seu cadastro aguarda autorização do administrador.");
            }
            
            return usuarioExiste;
            
        } else {
            throw new AccessDeniedException("Usuário não cadastrado!");
        }
    }
    
    private void validarDados(Usuario usuario) {
        if(usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email do usuário precisa ser preenchido!");
        }
        if(usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha do usuário precisa ser preenchida!");
        }
        validadorSenha.validar(usuario.getSenha());
    }
    
}
