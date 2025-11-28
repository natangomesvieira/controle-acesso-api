package br.com.controleacesso.factory;

import br.com.controleacesso.GerenciadorDeTelas;
import br.com.controleacesso.IViewFactory;
import br.com.controleacesso.presenter.CadastrarPresenter;
import br.com.controleacesso.view.CadastrarView;
import javax.swing.JInternalFrame;

public class CadastrarFactory implements IViewFactory {
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas navegador) {
        CadastrarView view = new CadastrarView();
        CadastrarPresenter presenter = new CadastrarPresenter(view, navegador);
        view.setPresenter(presenter);
        
        return view;
    }
    
}
