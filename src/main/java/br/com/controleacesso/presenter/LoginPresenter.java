package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.LoginView;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        view.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String response = login();
                    //logger.log(new LogEntry("CADASTRO_USUARIO", response));
                    JOptionPane.showMessageDialog(view, "Aviso: " + response);
                } catch (Exception ex) {
                    //logger.log(new LogEntry("CADASTRO_USUARIO", "novo_usuario", ex.getMessage()));
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });
        
        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cancelar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });
    }
    
    private void cancelar() {
        view.dispose();
    }
    
    private String login() {
        Usuario usuario = new Usuario();
        usuario.setEmail(view.getTxtEmail().getText());
        usuario.setSenha(view.getPwdSenha().getText());
        
        return service.login(usuario);
    }
    
}
