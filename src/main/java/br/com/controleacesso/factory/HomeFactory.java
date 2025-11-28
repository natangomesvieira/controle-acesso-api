package br.com.controleacesso.factory;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.presenter.HomePresenter;
import br.com.controleacesso.view.HomeView;

public class HomeFactory {
    
    public HomeView criarTela(GerenciadorDeTelas navegador) {
        HomeView view = new HomeView();
        HomePresenter presenter = new HomePresenter(view, navegador);
        view.setPresenter(presenter);
        
        return view;
    }
    
}
