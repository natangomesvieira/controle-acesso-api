package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.repository.impl.UsuarioRepositoryImpl;
import br.com.controleacesso.presenter.CadastroPresenter;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.repository.impl.NotificacaoRepositoryImpl;
import br.com.controleacesso.service.ICadastroService;
import br.com.controleacesso.service.impl.CadastroServiceImpl;
import br.com.controleacesso.view.CadastroView;
import br.com.sistemalog.LogService;
import javax.swing.JInternalFrame;

public class CadastroFactory implements IViewFactory {
    
    private final LogService logger;
    private final boolean cadastroObrigatorio;
    
    public CadastroFactory(LogService logger, boolean cadastroObrigatorio) {
        this.logger = logger;
        this.cadastroObrigatorio = cadastroObrigatorio;
    }
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        CadastroView view = new CadastroView();
        
        if (cadastroObrigatorio) {
            view.setClosable(false);
            view.setIconifiable(false);
            view.setMaximizable(false);
            view.setResizable(false);
            view.setTitle("Configuração Inicial - Cadastro de Administrador");
        } else {
            view.setClosable(true);
            view.setIconifiable(true);
            view.setResizable(true);
            view.setTitle("Cadastro de Usuário");
        }
        
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        INotificacaoRepository notificacaoRepository = new NotificacaoRepositoryImpl();
        ICadastroService service = new CadastroServiceImpl(usuarioRepository, notificacaoRepository);
        
        new CadastroPresenter(view, nav, service, logger, cadastroObrigatorio, sessao);
        
        return view;
    }
    
}
