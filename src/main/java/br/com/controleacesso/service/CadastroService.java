package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;
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
            e.printStackTrace();
            return false;
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
        
        String senhaValidar = Optional.ofNullable(usuario.getSenha()).orElse("");
        validadorSenha.validar(senhaValidar);
        
        boolean existeUsuario = repository.getAllUsers();
        usuario.setPerfil(!existeUsuario ? "administrador" : "usuario_padrao"); //Melhorar

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

        Optional.ofNullable(usuario.getSenha())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo Senha precisa ser preenchido!"));
    }
    
}
