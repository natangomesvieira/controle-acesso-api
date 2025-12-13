/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.presenter.RestaurarSistemaPresenter;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.RestaurarSistemaView;
import br.com.sistemalog.LogService;
import com.pss.senha.validacao.ValidadorSenha;
import javax.swing.JInternalFrame;

/**
 *
 * @author pedro
 */
public class RestaurarSistemaFactory implements IViewFactory {
    
    private final LogService logger;

    public RestaurarSistemaFactory(LogService logger) {
        this.logger = logger;
    }

    @Override
    public JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao) {
        RestaurarSistemaView view = new RestaurarSistemaView();
        UsuarioRepository repo = new UsuarioRepository();
        ValidadorSenha validador = new ValidadorSenha();
        DashboardService service = new DashboardService(repo, validador);
        
        new RestaurarSistemaPresenter(view, nav, service, logger, sessao);
        return view;
    }
}
