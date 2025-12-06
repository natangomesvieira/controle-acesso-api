package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.presenter.CadastroPresenter;
import br.com.controleacesso.service.CadastroService;
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
    public JInternalFrame criarTela(GerenciadorDeTelas nav) {
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
        
        UsuarioRepository repository = new UsuarioRepository();
        CadastroService service = new CadastroService(repository);
        
        new CadastroPresenter(view, nav, service, logger, cadastroObrigatorio);
        
        return view;
    }
    
}
