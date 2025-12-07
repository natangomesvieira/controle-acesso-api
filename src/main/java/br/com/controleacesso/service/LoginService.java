package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.function.Predicate;

public class LoginService {
    
    private final UsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    private static final String MSG_ERRO_GENERICA = "Usuário ou senha inválidos";
    
    public LoginService(UsuarioRepository repository) {
        this.repository = repository;
        this.validadorSenha = new ValidadorSenha();
    }
    
    public Usuario login(Usuario usuario) throws Exception {
        
        validarDados(usuario);

/*        Usuario usuarioExiste = repository.getByEmail(usuario.getEmail());
        
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
*/
       Usuario usuarioBanco = Optional.ofNullable(repository.getByEmail(usuario.getEmail()))
                .orElseThrow(() -> new AccessDeniedException(MSG_ERRO_GENERICA));
        
        if (!usuario.getSenha().equals(usuarioBanco.getSenha())) {
            throw new AccessDeniedException(MSG_ERRO_GENERICA);
        }

        if (!usuarioBanco.isAutorizado()) {
             throw new AccessDeniedException("Seu cadastro aguarda autorização do administrador.");
        }
        
        return usuarioBanco;
    }
    
    private void validarDados(Usuario usuario) {
        Optional.ofNullable(usuario.getEmail())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O email do usuário precisa ser preenchido!"));

        String senha = Optional.ofNullable(usuario.getSenha())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("A senha do usuário precisa ser preenchida!"));
            
        validadorSenha.validar(senha);
    }
    
}
