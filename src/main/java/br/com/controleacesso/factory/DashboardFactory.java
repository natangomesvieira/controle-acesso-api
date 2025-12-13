package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.presenter.DashboardPresenter;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.service.IDashboardService;
import br.com.controleacesso.service.impl.DashboardServiceImpl;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JInternalFrame;

public class DashboardFactory implements IViewFactory {
    
    private final LogService logger;
    private Usuario usuario;
    
    public DashboardFactory(LogService logger, Usuario usuario) {
        this.logger = logger;
        this.usuario = usuario;
    }
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        DashboardView view = new DashboardView();
        IUsuarioRepository repository = new UsuarioRepositoryImpl();
        ValidadorSenha validador = new ValidadorSenha();
        IDashboardService service = new DashboardServiceImpl(repository, validador);
        new DashboardPresenter(view, nav, service, logger, sessao, usuario);
        
        return view;
    }
    
}
