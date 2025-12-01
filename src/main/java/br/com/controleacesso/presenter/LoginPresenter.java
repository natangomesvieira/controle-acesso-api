package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.LoginService;
import br.com.controleacesso.view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginPresenter {
    
    private final LoginView view;
    private final GerenciadorDeTelas nav;
    private final LoginService service;
    
    public LoginPresenter (LoginView view, GerenciadorDeTelas nav, LoginService service) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String response = login();
                    JOptionPane.showMessageDialog(view, "Aviso: " + response);
                } catch (Exception ex) {
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
