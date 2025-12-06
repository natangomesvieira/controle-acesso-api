package br.com.controleacesso.factory;

import br.com.controleacesso.presenter.DashboardPresenter;
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
        new DashboardPresenter(view, nav, logger);
        
        return view;
    }
    
}
