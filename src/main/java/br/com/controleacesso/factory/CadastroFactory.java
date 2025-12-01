package br.com.controleacesso.factory;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.presenter.CadastroPresenter;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.CadastroView;
import javax.swing.JInternalFrame;

public class CadastroFactory implements IViewFactory {
    
    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav) {
        CadastroView view = new CadastroView();
        UsuarioRepository repository = new UsuarioRepository();
        CadastroService service = new CadastroService(repository);
        new CadastroPresenter(view, nav, service);
        
        return view;
    }
    
}
