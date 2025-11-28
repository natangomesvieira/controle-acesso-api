package br.com.controleacesso.factory;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.dao.UsuarioDAO;
import br.com.controleacesso.presenter.CadastroPresenter;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.CadastroView;
import javax.swing.JInternalFrame;

public class CadastroFactory implements IViewFactory {
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas navegador) {
        CadastroView view = new CadastroView();
        UsuarioDAO dao = new UsuarioDAO();
        CadastroService service = new CadastroService(dao);
        CadastroPresenter presenter = new CadastroPresenter(view, navegador, service);
        view.setPresenter(presenter);
        
        return view;
    }
    
}
