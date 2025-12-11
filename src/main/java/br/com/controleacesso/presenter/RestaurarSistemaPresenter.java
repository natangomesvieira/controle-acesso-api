/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controleacesso.presenter;

import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.RestaurarSistemaView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.Cursor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author pedro
 */
public class RestaurarSistemaPresenter {
    private final RestaurarSistemaView view;
    private final GerenciadorDeTelas nav;
    private final DashboardService service;
    private final LogService logger;

    public RestaurarSistemaPresenter(RestaurarSistemaView view, GerenciadorDeTelas nav, DashboardService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnCancelar().addActionListener(e -> view.dispose());
        view.getBtnRestaurar().addActionListener(e -> tentarRestaurar());
    }
    
    private void tentarRestaurar() {
        try {
            String senha = new String(view.getPwdSenha().getPassword());
            String conf = new String(view.getPwdConfirmacao().getPassword());
            int idAdmin = nav.getSessao().getIdUsuarioLogado();

            if(senha.isEmpty() || conf.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Preencha os dois campos de senha.");
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "ESTA É UMA OPERAÇÃO DESTRUTIVA!\n\n" +
                    "Todos os dados (usuários, registros, configurações) serão apagados.\n" +
                    "O sistema voltará ao estado inicial.\n\n" +
                    "Deseja realmente continuar?",
                    "Restauração do Sistema",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                executarRestauracao(idAdmin, senha, conf);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro: " + ex.getMessage());
        }
    }
    
    private void executarRestauracao(int idAdmin, String senha, String conf) {

        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        SwingUtilities.invokeLater(() -> {
            try {

                service.restaurarSistema(idAdmin, senha, conf);

                //logger.log(new LogEntry("RESTAURACAO_SISTEMA", "ID: " + idAdmin, "SUCESSO", "Sistema resetado"));

                view.setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(view, 
                        "Restauração concluída com sucesso!\nO sistema será reiniciado para o cadastro inicial.", 
                        "Concluído", 
                        JOptionPane.INFORMATION_MESSAGE);

                reinicializarAplicacao();

            } catch (Exception ex) {
                view.setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(view, "Falha na restauração: " + ex.getMessage(), "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void reinicializarAplicacao() {
        view.dispose();
        nav.limparSessao();
        
        nav.abrirTela(new br.com.controleacesso.factory.RestaurarSistemaFactory(logger));
    }
}
