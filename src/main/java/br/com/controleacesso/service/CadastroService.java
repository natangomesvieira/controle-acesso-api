package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CadastroService {
    
    private final UsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    
    public CadastroService(UsuarioRepository repository) {
        this.repository = repository;
        this.validadorSenha = new ValidadorSenha();
    }
    
    public boolean cadastroInicial() {
        try {
            return !repository.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar estado inicial do banco de dados: " + e.getMessage(), e);
        }
    }
    
    public void criarUsuario(Usuario usuario) throws Exception {
 
        validarCamposObrigatorios(usuario);
        
        List<String> erros = validadorSenha.validar(usuario.getSenha());
        
        if (erros != null && !erros.isEmpty()) {
            throw new IllegalArgumentException("Senha fraca:\n- " + String.join("\n- ", erros));
        }
        
        try {
            boolean existeUsuario = repository.getAllUsers();
            
            /* if (!existeUsuario) {
                // US 01: Primeiro usuário é Admin e já pode entrar
                usuario.setPerfil("administrador");
                usuario.setAutorizado(true); 
            } else {
                // US 03: Próximos usuários são Padrão e aguardam aprovação
                usuario.setPerfil("usuario_padrao");
                usuario.setAutorizado(false); 
            } */
            
            String perfil = verificarPerfil(null, existeUsuario);
            usuario.setPerfil(perfil);
            
            boolean isAdmin = "administrador".equals(perfil);
            usuario.setAutorizado(isAdmin);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar usuários: " + e.getMessage(), e);
        }
        repository.salvar(usuario);
    }
    
    
    public String verificarPerfil(String perfil, boolean existeUsuario) {

        if (perfil != null && !perfil.isBlank()) {
            return perfil;
        }

        if (!existeUsuario) {
            return "administrador";
        } 

        return "usuario_padrao";
    }
    
    private void validarCamposObrigatorios(Usuario usuario) {
        // Valida o objeto pai
        Optional.ofNullable(usuario)
            .orElseThrow(() -> new IllegalArgumentException("O objeto usuário não pode ser nulo!"));
        
        // Valida Nome (Não nulo E não vazio/espaços)
        Optional.ofNullable(usuario.getNome())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo Nome precisa ser preenchido!"));

        // Valida E-mail
        Optional.ofNullable(usuario.getEmail())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo E-mail precisa ser preenchido!"));
        
        // Validação da existencia da senha
        Optional.ofNullable(usuario.getSenha())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo Senha precisa ser preenchido!"));
        
        // Validação da existencia e igualdade da confSenha
        String confSenha = Optional.ofNullable(usuario.getConfSenha()).orElse("");
        if (!usuario.getSenha().equals(confSenha)) {
            throw new IllegalArgumentException("A confirmação da senha não confere!");
        }
    }
    
}
