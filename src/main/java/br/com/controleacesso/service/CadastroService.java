package br.com.controleacesso.service;

import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.model.Usuario;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;

public class CadastroService {
    
    private final UsuarioRepository repository;
    //private final LogService logger;
    
    public CadastroService(UsuarioRepository repository) {
        this.repository = repository;
        //this.logger = logger;
    }
    
    public void criarUsuario(Usuario usuario) {
        try {
            validarSenha(usuario.getSenha(), usuario.getConfSenha());
            
            boolean existeUsuario = repository.temUsuariosCadastrados();
            usuario.setPerfil(!existeUsuario ? "administrador" : "usuario_padrao");
            repository.salvar(usuario);
            
            //log de sucesso aqui
            //logger.log(new LogEntry("INCLUSAO_USUARIO", "SUCESSO", "Perfil: " + usuario.getPerfil()));
            
        } catch (Exception e) {
            // Log de Falha
            
        }
    }
    
    private void validarSenha(String senha, String confSenha) {
        if(!senha.equals(confSenha)) {
            throw new IllegalArgumentException("As senhas não são iguais!");
        }
    }
    
}
