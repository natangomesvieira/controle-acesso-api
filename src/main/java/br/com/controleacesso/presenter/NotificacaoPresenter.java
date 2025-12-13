package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.model.Notificacao;
import br.com.controleacesso.service.INotificacaoService;
import br.com.controleacesso.view.NotificacaoView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NotificacaoPresenter {
    
    private final NotificacaoView view;
    private final INotificacaoService service;
    private final LogService logger;
    private ContextoDeSessao sessao;
    private List<Notificacao> notificacoes;
    
    public NotificacaoPresenter (NotificacaoView view, INotificacaoService service, LogService logger, ContextoDeSessao sessao) {
        this.view = view;
        this.service = service;
        this.logger = logger;
        this.sessao = sessao;
        carregarTabelaNotificacoes();
        configuraView();
    }
    
    private void configuraView() {
        view.getBtnLido().addActionListener((ActionEvent e) -> {
            marcarComoLido();
        });
    }
    
    private void marcarComoLido() {
        try {
            service.marcarComoLido(sessao.getIdUsuarioLogado());
            logger.log(new LogEntry("MARCAR_COMO_LIDO_NOTIFICACOES", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));
            JOptionPane.showMessageDialog(view, "Notificações marcadas como lida");
        } catch (Exception ex) {
            logger.log(new LogEntry("MARCAR_COMO_LIDO_NOTIFICACOES", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao marcar notificações como lida: " + ex.getMessage());
        }
    }
    
    private void carregarTabelaNotificacoes() {

        String[] colunas = {"Mensagem", "Data"};

        try { 
            notificacoes = service.getNotificacoesNaoLidas(sessao.getIdUsuarioLogado());
            logger.log(new LogEntry("LISTAGEM_NOTIFICACOES", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));
        } catch (Exception e) {
            logger.log(new LogEntry("LISTAGEM_NOTIFICACOES", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), e.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao carregar usuários: " + e.getMessage());
            return;
        }
        
        Object[][] dadosTabela = converterListaParaArray(notificacoes);

        DefaultTableModel tableModel = new DefaultTableModel(dadosTabela, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        view.getNotificacoes().setModel(tableModel);
    }
    
        private Object[][] converterListaParaArray(List<Notificacao> notif) {
        if (notif == null || notif.isEmpty()) {
            return new Object[0][2];
        }

        Object[][] dados = new Object[notif.size()][2];

        for (int i = 0; i < notif.size(); i++) {
            Notificacao u = notif.get(i);
            dados[i][0] = u.getMensagem();
            dados[i][1] = u.getData();
        }
        return dados;
    }
}
