package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.presenter.NotificacaoPresenter;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.impl.NotificacaoRepositoryImpl;
import br.com.controleacesso.service.INotificacaoService;
import br.com.controleacesso.service.impl.NotificacaoServiceImpl;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.NotificacaoView;
import br.com.sistemalog.LogService;
import javax.swing.JInternalFrame;

public class NotificacaoFactory implements IViewFactory {
    
    private final LogService logger;

    public NotificacaoFactory(LogService logger) {
        this.logger = logger;
    }

    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        NotificacaoView view = new NotificacaoView();
        view.setTitle("Notificações");
        view.setClosable(true);
        view.setIconifiable(true);
        
        INotificacaoRepository repository = new NotificacaoRepositoryImpl();
        INotificacaoService service = new NotificacaoServiceImpl(repository);
        
        new NotificacaoPresenter(view, service, logger, sessao);
        
        return view;
    }
    
}
