package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.presenter.RestaurarSistemaPresenter;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.service.IDashboardService;
import br.com.controleacesso.service.impl.DashboardServiceImpl;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.RestaurarSistemaView;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JInternalFrame;

public class RestaurarSistemaFactory implements IViewFactory {
    
    private final LogService logger;

    public RestaurarSistemaFactory(LogService logger) {
        this.logger = logger;
    }

    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        RestaurarSistemaView view = new RestaurarSistemaView();
        IUsuarioRepository repo = new UsuarioRepositoryImpl();
        ValidadorSenha validador = new ValidadorSenha();
        IDashboardService service = new DashboardServiceImpl(repo, validador);
        
        new RestaurarSistemaPresenter(view, nav, service, logger, sessao);
        return view;
    }
}
