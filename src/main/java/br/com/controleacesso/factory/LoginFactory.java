package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.presenter.LoginPresenter;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.NotificacaoRepositoryImpl;
import br.com.controleacesso.service.ILoginService;
import br.com.controleacesso.service.impl.LoginServiceImpl;
import br.com.controleacesso.view.LoginView;
import br.com.sistemalog.LogService;
import javax.swing.JInternalFrame;

public class LoginFactory implements IViewFactory {
    
    private final LogService logger;
    
    public LoginFactory(LogService logger) {
        this.logger = logger;
    }
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        LoginView view = new LoginView();
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        INotificacaoRepository notificacaoRepository = new NotificacaoRepositoryImpl();
        ILoginService service = new LoginServiceImpl(usuarioRepository, notificacaoRepository);
        new LoginPresenter(view, nav, service, logger);
        
        return view;
    }
    
}
