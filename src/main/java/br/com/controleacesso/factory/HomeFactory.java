package br.com.controleacesso.factory;

import br.com.controleacesso.presenter.HomePresenter;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.HomeView;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JFrame;

public class HomeFactory {
    
    private final LogService logger;
    
    public HomeFactory(LogService logger) {
        this.logger = logger;
    }
    
    public JFrame criarTela() {
        HomeView view = new HomeView();
        GerenciadorDeTelas nav = new GerenciadorDeTelas(view.getDesktop());
        UsuarioRepository repository = new UsuarioRepository();
        CadastroService service = new CadastroService(repository);
        new HomePresenter(view, nav, service, logger);
        
        return view;
    }
    
}
