package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.factory.LoginFactory;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.HomeView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class HomePresenter {
    
    private final HomeView view;
    private final GerenciadorDeTelas nav;
    private final LogService logger;
    private final CadastroService service;
    
    public HomePresenter(HomeView view, GerenciadorDeTelas nav, CadastroService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
        iniciarFluxo();
    }
    
    private void iniciarFluxo() {
        if (service.cadastroInicial()) {
            JOptionPane.showMessageDialog(view, """
                                                Bem-vindo(a)! Nenhuma conta detectada. 
                                                Iniciaremos o cadastro do Administrador do sistema.""");
            view.getBtnEntrar().setEnabled(false);
            irParaCadastro(true);
        } else {
            irParaLogin();
        }
    }
    
    private void configuraView() {
        view.setVisible(false);
        view.getBtnCadastrar().addActionListener((ActionEvent e) -> {
            try {
                logger.log(new LogEntry("LOGIN_USUARIO", "TESTE"));
                irParaCadastro(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Falha:" + ex.getMessage());
            }
        });
       
        view.getBtnEntrar().addActionListener((ActionEvent e) -> {
            try {
                irParaLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Falha:" + ex.getMessage());
            }
        });
        view.setVisible(true);
        view.setLocationRelativeTo(null);
   }
    
    private void irParaLogin() {
       nav.abrirTela(new LoginFactory(logger));
    }
    
    private void irParaCadastro(boolean obrigatorio) {
        nav.abrirTela(new CadastroFactory(logger, obrigatorio));
    }
    
}
