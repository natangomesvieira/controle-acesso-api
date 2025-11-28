package br.com.controleacesso.presenter;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.factory.CadastrarFactory;
import br.com.controleacesso.view.interfaces.IHomeView;

public class HomePresenter {
    
    private final IHomeView view;
    private final GerenciadorDeTelas navegador;
    
    public HomePresenter(IHomeView view, GerenciadorDeTelas navegador) {
        this.view = view;
        this.navegador = navegador;
    }
    
    public void irParaLogin() {
       
    }
    
    public void irParaCadastro() {
        navegador.abrirTela(new CadastrarFactory());
    }
    
}
