package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class DashboardPresenter {
    
    private final DashboardView view;
    private final GerenciadorDeTelas nav;
    private final LogService logger;
    
    public DashboardPresenter(DashboardView view, GerenciadorDeTelas nav, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.logger = logger;
        configuraAcessoAoPerfil();
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnNovoUsuario().addActionListener((ActionEvent e) -> {
            irParaCadastro();
        });
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                nav.limparSessao();
            }
        });
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
        ContextoDeSessao sessao = nav.getSessao();
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }
        
        view.getBtnNovoUsuario().setVisible(isAdminLogado); 
    }
    
    private void irParaCadastro() {
       nav.abrirTela(new CadastroFactory(logger, false));
    }
        
}
