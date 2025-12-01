package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.CadastroView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CadastroPresenter {
    
    private final CadastroView view;
    private final GerenciadorDeTelas nav;
    private final CadastroService service;
    private final LogService logger;

    public CadastroPresenter(CadastroView view, GerenciadorDeTelas nav, CadastroService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
    }

    private void configuraView() {
        view.getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cadastrar();
                    //logger.log(new LogEntry("CADASTRO_USUARIO", "novo_usuario"));
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
