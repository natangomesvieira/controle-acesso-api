package br.com.controleacesso.service.impl;

import br.com.controleacesso.model.Notificacao;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.service.ILoginService;
import com.pss.senha.validacao.ValidadorSenha;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LoginServiceImpl implements ILoginService {
    
    private final IUsuarioRepository usuarioRepository;
    private final ValidadorSenha validadorSenha;
    private final INotificacaoRepository notificacaoRepository;
    private static final String MSG_ERRO_GENERICA = "Usuário ou senha inválidos";
    
    public LoginServiceImpl(IUsuarioRepository usuarioRepository, INotificacaoRepository notificacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.validadorSenha = new ValidadorSenha();
    }
    
    @Override
    public Usuario login(Usuario usuario) throws Exception {
        
       validarDados(usuario);

       Usuario usuarioBanco = Optional.ofNullable(usuarioRepository.getByEmail(usuario.getEmail()))
                .orElseThrow(() -> new AccessDeniedException(MSG_ERRO_GENERICA));
        
        if (!usuario.getSenha().equals(usuarioBanco.getSenha())) {
            throw new AccessDeniedException(MSG_ERRO_GENERICA);
        }

        if (!usuarioBanco.isAutorizado()) {
             throw new AccessDeniedException("Seu cadastro aguarda autorização do administrador.");
        }
        
        List<Notificacao> pendentes = notificacaoRepository.getNotificacoesNaoLidas(usuarioBanco.getId());
            
        if (!pendentes.isEmpty()) {
            usuarioBanco.setNotificacoes(pendentes);
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
