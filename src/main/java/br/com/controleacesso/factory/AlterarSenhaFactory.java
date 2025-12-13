package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.presenter.AlterarSenhaPresenter;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.service.IDashboardService;
import br.com.controleacesso.service.impl.DashboardServiceImpl;
import br.com.controleacesso.view.AlterarSenhaView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JInternalFrame;

public class AlterarSenhaFactory implements IViewFactory {

    private final LogService logger;

    public AlterarSenhaFactory(LogService logger) {
        this.logger = logger;
    }

    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        AlterarSenhaView view = new AlterarSenhaView();
        view.setTitle("Alterar Senha");
        view.setClosable(true);
        view.setIconifiable(true);
        
        IUsuarioRepository repository = new UsuarioRepositoryImpl();
        ValidadorSenha validador = new ValidadorSenha();
        
        IDashboardService service = new DashboardServiceImpl(repository, validador);
        
        new AlterarSenhaPresenter(view, service, logger, sessao);
        
        return view;
    }
}
