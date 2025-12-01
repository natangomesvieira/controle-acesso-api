package br.com.controleacesso.factory;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.presenter.LoginPresenter;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.LoginView;
import javax.swing.JInternalFrame;

public class LoginFactory implements IViewFactory {
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav) {
        LoginView view = new LoginView();
        UsuarioRepository repository = new UsuarioRepository();
        LoginService service = new LoginService(repository);
        new LoginPresenter(view, nav, service);
        
        return view;
    }
    
}
