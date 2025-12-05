package br.com.controleacesso.presenter;

import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPresenter {
    
    private final DashboardView view;
    private final GerenciadorDeTelas nav;
    private final CadastroService service;
    private final LogService logger;
    
    public DashboardPresenter(DashboardView view, GerenciadorDeTelas nav, CadastroService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnNovoUsuario().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                irParaCadastro();
            }
        });
    }
    
    private void irParaCadastro() {
       // nav.abrirTela(new CadastroFactory(logger));
    }
        
}
