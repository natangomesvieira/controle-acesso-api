package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.DashboardFactory;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.LoginView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class LoginPresenter {
    
    private final LoginView view;
    private final GerenciadorDeTelas nav;
    private final LoginService service;
    private final LogService logger;
    
    public LoginPresenter (LoginView view, GerenciadorDeTelas nav, LoginService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnEntrar().addActionListener((ActionEvent e) -> {
            login();
        });
        
        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            try {
                fecharJanela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
            }
        });
    }
    
    private void fecharJanela() {
        view.dispose();
    }
    
    private void login() {
        
        Usuario usuario = new Usuario();
        
        try {
            usuario.setEmail(view.getTxtEmail().getText());
            usuario.setSenha(view.getPwdSenha().getText());
        
            usuario = service.login(usuario);
            
            ContextoDeSessao sessao = new ContextoDeSessao(usuario.getId(), usuario.getPerfil());

            nav.setSessao(sessao);
            
            logger.log(new LogEntry("LOGIN_USUARIO", usuario.getNome()));
            
            fecharJanela();
            
            nav.abrirTela(new DashboardFactory(logger));
        } catch (Exception ex) {
            logger.log(new LogEntry("LOGIN_USUARIO", usuario.getEmail(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
        
    }
    
}
