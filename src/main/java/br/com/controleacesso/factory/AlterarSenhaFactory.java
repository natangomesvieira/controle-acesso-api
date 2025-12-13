/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.presenter.AlterarSenhaPresenter;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.AlterarSenhaView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JInternalFrame;

/**
 *
 * @author pedro
 */
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
        
        UsuarioRepository repository = new UsuarioRepository();
        ValidadorSenha validador = new ValidadorSenha();
        
        DashboardService service = new DashboardService(repository, validador);
        
        new AlterarSenhaPresenter(view, service, logger, sessao);
        
        return view;
    }
}
