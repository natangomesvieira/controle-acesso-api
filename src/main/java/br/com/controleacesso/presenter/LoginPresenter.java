package br.com.controleacesso.presenter;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.view.ILoginView;

public class LoginPresenter {
    
    private final ILoginView view;
    private final GerenciadorDeTelas navegador;

    public LoginPresenter(ILoginView view, GerenciadorDeTelas navegador) {
        this.view = view;
        this.navegador = navegador;
    }

    public void login() {
        String email = view.getEmail();
        String senha = view.getSenha();

        if (email.equals("admin@email.com") && senha.equals("123")) {
            view.mostrarMensagem("Login com Sucesso!");
            view.fecharTela();
        } else {
            view.mostrarMensagem("Usuário ou senha inválidos!");
        }
    }
    
    public void cadastrar() {
        
    }
    
}
