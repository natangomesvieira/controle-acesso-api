package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import com.pss.senha.validacao.ValidadorSenha;

public class CadastroService {
    
    private final UsuarioRepository repository;
    private final ValidadorSenha validadorSenha;
    
    public CadastroService(UsuarioRepository repository) {
        this.repository = repository;
        this.validadorSenha = new ValidadorSenha();
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
        validadorSenha.validar(usuario.getSenha());
        
        boolean existeUsuario = repository.temUsuariosCadastrados();
        usuario.setPerfil(!existeUsuario ? "administrador" : "usuario_padrao");

        repository.salvar(usuario);
    }
    
}
