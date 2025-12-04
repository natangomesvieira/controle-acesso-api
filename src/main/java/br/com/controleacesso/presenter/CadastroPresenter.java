package br.com.controleacesso.presenter;

import br.com.controleacesso.factory.DashboardFactory;
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
                cadastrar();
            }
        });
        
        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }
    
    private void cadastrar() {
        
        Usuario usuario = new Usuario();
            
        try {
            
            usuario.setNome(view.getTxtNome().getText());
            usuario.setEmail(view.getTxtEmail().getText());
            usuario.setSenha(view.getPwdSenha().getText());
            usuario.setConfSenha(view.getPwdConfSenha().getText());

            service.criarUsuario(usuario);
            
            logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome()));
            
            nav.abrirTela(new DashboardFactory(logger));
        } catch (Exception ex) {
            logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }

    }
    
    private void cancelar() {
        view.dispose();
    }
    
}
