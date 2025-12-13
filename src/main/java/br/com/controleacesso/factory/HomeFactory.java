package br.com.controleacesso.factory;

import br.com.controleacesso.presenter.HomePresenter;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.NotificacaoRepositoryImpl;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.service.ICadastroService;
import br.com.controleacesso.service.impl.CadastroServiceImpl;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.HomeView;
import br.com.sistemalog.LogService;
import javax.swing.JFrame;

public class HomeFactory {
    
    private final LogService logger;
    
    public HomeFactory(LogService logger) {
        this.logger = logger;
    }
    
    public JFrame criarTela() {
        HomeView view = new HomeView();
        GerenciadorDeTelas nav = new GerenciadorDeTelas(view.getDesktop());
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        INotificacaoRepository notificacaoRepository = new NotificacaoRepositoryImpl();
        ICadastroService service = new CadastroServiceImpl(usuarioRepository, notificacaoRepository);
        new HomePresenter(view, nav, service, logger);
        
        return view;
    }
    
}
