package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

public class DashboardPresenter {
    
    private final DashboardView view;
    private final GerenciadorDeTelas nav;
    private final LogService logger;
    private final DashboardService service;
    
    public DashboardPresenter(DashboardView view, GerenciadorDeTelas nav, DashboardService service, LogService logger) {
        this.view = view;
        this.nav = nav;
        this.logger = logger;
        this.service = service;
        configuraView();
    }
    
    private void configuraView() {
        configuraAcessoAoPerfil();
        carregarTabelaUsuarios();
        view.getBtnNovoUsuario().addActionListener((ActionEvent e) -> {
            irParaCadastro();
        });
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                nav.limparSessao();
            }
        });
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
        ContextoDeSessao sessao = nav.getSessao();
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }
        
        view.getBtnNovoUsuario().setVisible(isAdminLogado); 
    }
    
    private void irParaCadastro() {
       nav.abrirTela(new CadastroFactory(logger, false));
    }
    
    private void carregarTabelaUsuarios() {
    
        String[] colunas = {"Nome", "Email", "Perfil"};

        List<Usuario> listaUsuarios = null;
        try {
            listaUsuarios = service.getAllUsuarios(); 
        } catch (Exception e) {
            logger.log(new LogEntry("ERRO_LISTAGEM", e.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao carregar usuários: " + e.getMessage());
            return;
        }

        Object[][] dadosTabela = converterListaParaArray(listaUsuarios);

        DefaultTableModel tableModel = new DefaultTableModel(dadosTabela, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        view.getTabelaUsuarios().setModel(tableModel);
    }

    private Object[][] converterListaParaArray(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            return new Object[0][3];
        }

        Object[][] dados = new Object[usuarios.size()][4];

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            dados[i][0] = u.getNome();
            dados[i][1] = u.getEmail();
            dados[i][2] = u.getPerfil();
        }
        return dados;
    }
        
}
