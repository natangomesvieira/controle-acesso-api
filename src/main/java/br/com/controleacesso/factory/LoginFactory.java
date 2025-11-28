package br.com.controleacesso.factory;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.dao.UsuarioDAO;
import br.com.controleacesso.presenter.LoginPresenter;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.LoginView;
import javax.swing.JInternalFrame;

public class LoginFactory implements IViewFactory {
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas navegador) {
        LoginView view = new LoginView();
        UsuarioDAO dao = new UsuarioDAO();
        LoginService service = new LoginService(dao);
        LoginPresenter presenter = new LoginPresenter(view, navegador, service);
        view.setPresenter(presenter);
        
        return view;
    }
    
}
