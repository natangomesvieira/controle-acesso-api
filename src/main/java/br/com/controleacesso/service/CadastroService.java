package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;
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
        } catch (Exception e) {
            throw new RuntimeException("Falha ao verificar estado inicial do banco de dados: " + e.getMessage(), e);
        }
    }
    
    public void criarUsuario(Usuario usuario) throws Exception {
        //TODO: Aplicar demais validações
        /*
        1- Validar se os dados do usuário não são null. 
            (crie uma função private pra isso e caso algum dado seja null dispare uma excessão
             throw new IllegalArgumentException("O campo X precisa ser preenchido!");)
        2- Verificar se é o primeiro usuário a ser criado, se sim deve ser ADMIN
        3- 
        */
        validarCamposObrigatorios(usuario);
        
        List<String> erros = validadorSenha.validar(usuario.getSenha());
        
        if (erros != null && !erros.isEmpty()) {
            throw new IllegalArgumentException("Senha fraca:\n- " + String.join("\n- ", erros));
        }
        
        try {
            boolean existeUsuario = repository.getAllUsers();
            usuario.setPerfil(!existeUsuario ? "administrador" : "usuario_padrao"); //Melhorar
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar usuários: " + e.getMessage(), e);
        }
        repository.salvar(usuario);
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
