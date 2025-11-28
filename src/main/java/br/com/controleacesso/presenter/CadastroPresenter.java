package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.interfaces.ICadastroView;

public class CadastroPresenter {
    
    private final ICadastroView view;
    private final GerenciadorDeTelas navegador;
    private final CadastroService service;

    public CadastroPresenter(ICadastroView view, GerenciadorDeTelas navegador, CadastroService service) {
        this.view = view;
        this.navegador = navegador;
        this.service = service;
    }

    public void cadastrar() {
        
        Usuario usuario = new Usuario();
        
        usuario.setNome(view.getNome());
        usuario.setEmail(view.getEmail());
        usuario.setSenha(view.getSenha());
        usuario.setConfSenha(view.getConfSenha());
        
        service.criarUsuario(usuario);
       
        //view.fecharTela();
        //view.mostrarMensagem("Usuário ou senha inválidos!");
    }
    
}
