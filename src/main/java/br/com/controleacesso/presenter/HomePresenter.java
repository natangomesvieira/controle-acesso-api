package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.factory.LoginFactory;
import br.com.controleacesso.view.interfaces.IHomeView;

public class HomePresenter {
    
    private final IHomeView view;
    private final GerenciadorDeTelas navegador;
    
    public HomePresenter(IHomeView view, GerenciadorDeTelas navegador) {
        this.view = view;
        this.navegador = navegador;
    }
    
    public void irParaLogin() {
       navegador.abrirTela(new LoginFactory());
    }
    
    public void irParaCadastro() {
        navegador.abrirTela(new CadastroFactory());
    }
    
}
