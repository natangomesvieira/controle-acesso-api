package br.com.controleacesso.presenter;

import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.AlterarSenhaView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class AlterarSenhaPresenter {
    
    private final AlterarSenhaView view;
    private final GerenciadorDeTelas nav;
    private final DashboardService service;
    private final LogService logger;
    
    public AlterarSenhaPresenter(AlterarSenhaView view, GerenciadorDeTelas nav, DashboardService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnSalvar().addActionListener((ActionEvent e) -> {
            salvarSenha();
        });
        
        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            view.dispose();
        });
    }
    
    private void salvarSenha() {
        try {
            
            int idUsuario = nav.getSessao().getIdUsuarioLogado(); //usuario logado
            String senhaAtual = new String(view.getPwdSenhaAtual().getPassword());
            String novaSenha = new String(view.getPwdNovaSenha().getPassword());
            String confSenha = new String(view.getPwdConfSenha().getPassword());
            
            int confirmacao = JOptionPane.showConfirmDialog(view, 
                    "Tem certeza que deseja alterar sua senha?", 
                    "Confirmar Alteração", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                
                service.alterarSenha(idUsuario, senhaAtual, novaSenha, confSenha);
                //logger.log(new LogEntry("ALTERAR_SENHA", usuario.getNome(), usuario.getPerfil()));
                JOptionPane.showMessageDialog(view, "Senha alterada com sucesso!");
                view.dispose();
            }
        } catch (Exception ex) {
            //logger.log(new LogEntry("ALTERAR_SENHA", usuario.getNome(), usuario.getPerfil(), ex.getMessage()));
            String msgErro = ex.getMessage();
            JOptionPane.showMessageDialog(view, "Não foi possível alterar a senha:\n" + msgErro, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
