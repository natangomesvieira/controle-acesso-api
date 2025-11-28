package br.com.controleacesso.presenter;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.view.interfaces.ICadastrarView;

public class CadastrarPresenter {
    
    private final ICadastrarView view;
    private final GerenciadorDeTelas navegador;

    public CadastrarPresenter(ICadastrarView view, GerenciadorDeTelas navegador) {
        this.view = view;
        this.navegador = navegador;
    }

    public void login() {
        String email = view.getEmail();
        String senha = view.getSenha();

        if (email.equals("admin@email.com") && senha.equals("123")) {
            view.mostrarMensagem("Login com Sucesso!");
            //view.fecharTela();
        } else {
            view.mostrarMensagem("Usuário ou senha inválidos!");
        }
    }
    
    public void cadastrar() {
        
    }
    
}
