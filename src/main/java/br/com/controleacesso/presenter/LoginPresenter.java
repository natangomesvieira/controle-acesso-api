package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.interfaces.ILoginView;

public class LoginPresenter {
    
    private final ILoginView view;
    private final GerenciadorDeTelas navegador;
    private final LoginService service;
    
    public LoginPresenter (ILoginView view, GerenciadorDeTelas navegador, LoginService service) {
        this.view = view;
        this.navegador = navegador;
        this.service = service;
    }
    
    public void login() {
        Usuario usuario = new Usuario();
        usuario.setEmail(view.getEmail());
        usuario.setSenha(view.getSenha());
        
        String response = service.login(usuario);
        
        view.mostrarMensagem(response);
    }
    
}
