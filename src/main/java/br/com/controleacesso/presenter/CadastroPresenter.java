package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.CadastroView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CadastroPresenter {
    
    private CadastroView view;
    private GerenciadorDeTelas nav;
    private CadastroService service;

    public CadastroPresenter(CadastroView view, GerenciadorDeTelas nav, CadastroService service) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        configuraView();
    }

    private void configuraView() {
        view.getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cadastrar();
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
    
    private void cadastrar() {
        
        Usuario usuario = new Usuario();
        
        usuario.setNome(view.getTxtNome().getText());
        usuario.setEmail(view.getTxtEmail().getText());
        usuario.setSenha(view.getPwdSenha().getText());
        usuario.setConfSenha(view.getPwdConfSenha().getText());
        
        service.criarUsuario(usuario);

    }
    
    private void cancelar() {
        view.dispose();
    }
    
}
