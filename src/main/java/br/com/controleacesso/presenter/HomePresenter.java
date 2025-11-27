package br.com.controleacesso.presenter;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.view.IHomeView;

public class HomePresenter {
    
    private final IHomeView view;
    private final GerenciadorDeTelas navegador;
    
    public HomePresenter(IHomeView view, GerenciadorDeTelas navegador) {
        this.view = view;
        this.navegador = navegador;
    }
    
    public void irParaLogin() {
        view.fecharTela();
    }
    
    public void irParaCadastro() {
        view.fecharTela();
        navegador.telaCadastro();
    }
    
}
