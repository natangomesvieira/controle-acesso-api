package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.service.IDashboardService;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.view.RestaurarSistemaView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.Cursor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class RestaurarSistemaPresenter {
    private final RestaurarSistemaView view;
    private final GerenciadorDeTelas nav;
    private final IDashboardService service;
    private final LogService logger;
    private final ContextoDeSessao sessao;

    public RestaurarSistemaPresenter(RestaurarSistemaView view, GerenciadorDeTelas nav, IDashboardService service, LogService logger, ContextoDeSessao sessao) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        this.sessao = sessao;
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
            int idAdmin = sessao.getIdUsuarioLogado();

            if(senha.isEmpty() || conf.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Preencha os dois campos de senha.");
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(view, """
                                                                  ESTA \u00c9 UMA OPERA\u00c7\u00c3O DESTRUTIVA!
                                                                  
                                                                  Todos os dados (usu\u00e1rios, registros, configura\u00e7\u00f5es) ser\u00e3o apagados.
                                                                  O sistema voltar\u00e1 ao estado inicial.
                                                                  
                                                                  Deseja realmente continuar?""",
                    "Restauração do Sistema",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                executarRestauracao(idAdmin, senha, conf);
            }

        } catch (Exception ex) {
            logger.log(new LogEntry("RESTAURACAO_SISTEMA", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, "Erro: " + ex.getMessage());
        }
    }
    
    private void executarRestauracao(int idAdmin, String senha, String conf) {

        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        SwingUtilities.invokeLater(() -> {
            try {

                service.restaurarSistema(idAdmin, senha, conf);

                logger.log(new LogEntry("RESTAURACAO_SISTEMA", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));

                view.setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(view, 
                        "Restauração concluída com sucesso!\nO sistema será reiniciado para o cadastro inicial.", 
                        "Concluído", 
                        JOptionPane.INFORMATION_MESSAGE);

                reinicializarAplicacao();

            } catch (Exception ex) {
                logger.log(new LogEntry("RESTAURACAO_SISTEMA", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), ex.getMessage()));
                view.setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(view, "Falha na restauração: " + ex.getMessage(), "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void reinicializarAplicacao() {
        nav.fecharTodasAsTelas();
        
        nav.abrirTela(new br.com.controleacesso.factory.CadastroFactory(logger, true), null);
    }
}
