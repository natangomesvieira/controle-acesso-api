package br.com.controleacesso.factory;

import br.com.controleacesso.presenter.DashboardPresenter;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import javax.swing.JInternalFrame;

public class DashboardFactory implements IViewFactory {
    
        private final LogService logger;
    
    public DashboardFactory(LogService logger) {
        this.logger = logger;
    }
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav) {
        DashboardView view = new DashboardView();
        UsuarioRepository repository = new UsuarioRepository();
        DashboardService service = new DashboardService(repository);
        new DashboardPresenter(view, nav, service, logger);
        
        return view;
    }
    
}
