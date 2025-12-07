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
import javax.swing.JTable;
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
        configuraAcessoAoPerfil();
        configuraView();
    }
    
    private void configuraView() {
        carregarTabelaUsuarios(true);
        view.getBtnNovoUsuario().addActionListener((ActionEvent e) -> {
            irParaCadastro();
        });
        view.getBtnAutorizarAcesso().addActionListener((ActionEvent e) -> {
            autorizarAcesso();
        });
        view.getBtnRejeitarAcesso().addActionListener((ActionEvent e) -> {
            rejeitarAcesso();
        });
        view.getBtnPromoverUsuario().addActionListener((ActionEvent e) -> {
            promoverUsuario();
        });
        view.getBtnRebaixarUsuario().addActionListener((ActionEvent e) -> {
            rebaixarUsuario();
        });
        view.getBtnListagemUsuarios().addActionListener((ActionEvent e) -> {
            carregarTabelaUsuarios(true);
        });
        view.getBtnAutorizacoesPendentes().addActionListener((ActionEvent e) -> {
            carregarTabelaUsuarios(false);
        });
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                nav.limparSessao();
            }
        });
    }
    
    private void promoverUsuario() {
        try {
            JTable tabela = view.getTabelaUsuarios();
    
            int linhaSelecionada = tabela.getSelectedRow();
    
            if (linhaSelecionada == -1) {
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tabela.getModel().getValueAt(linhaSelecionada, 1);
            String perfil = (String) tabela.getModel().getValueAt(linhaSelecionada, 2);
            
            if(email != null) {
                service.promoverUsuario(email, perfil);
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");
                carregarTabelaUsuarios(false);
           }
        } catch (Exception ex) {
            //TODO: LOG
            JOptionPane.showMessageDialog(view, "Falha ao promover usuário!");
        }
    }
    
    private void rebaixarUsuario() {
        try {
            JTable tabela = view.getTabelaUsuarios();
    
            int linhaSelecionada = tabela.getSelectedRow();
    
            if (linhaSelecionada == -1) {
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tabela.getModel().getValueAt(linhaSelecionada, 1);
            String perfil = (String) tabela.getModel().getValueAt(linhaSelecionada, 2);
            
            if(email != null) {
                service.rebaixarUsuario(email, perfil);
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");
                carregarTabelaUsuarios(false);
           }
        } catch (Exception ex) {
            //TODO: LOG
            JOptionPane.showMessageDialog(view, "Falha ao promover usuário!");
        }
    }
    
    private void autorizarAcesso() {
        try {
           String email = emailUsuarioSelecionado();
           if(email != null) {
            service.autorizarAcessoByEmail(email);
            //TODO:LOG
            JOptionPane.showMessageDialog(view, "Usuário autorizado com sucesso!");
            carregarTabelaUsuarios(false);
           }
        } catch (Exception ex) {
            //TODO:LOG
            JOptionPane.showMessageDialog(view, "Falha ao realizar autorização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejeitarAcesso() {
        try {
           String email = emailUsuarioSelecionado();
           if(email != null) {
                service.rejeitarAcessoByEmail(email);
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Usuário rejeitado com sucesso!");
                carregarTabelaUsuarios(false);
           }
        } catch (Exception ex) {
            //TODO:LOG
            JOptionPane.showMessageDialog(view, "Falha ao realizar rejeição: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String emailUsuarioSelecionado() {
        JTable tabela = view.getTabelaUsuarios();
    
        int linhaSelecionada = tabela.getSelectedRow();
    
        if (linhaSelecionada == -1) {
            //TODO:LOG
            JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) tabela.getModel().getValueAt(linhaSelecionada, 1);
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
        ContextoDeSessao sessao = nav.getSessao();
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }
        
        view.getBtnListagemUsuarios().setVisible(isAdminLogado);
        view.getBtnNovoUsuario().setVisible(isAdminLogado); 
        view.getBtnAutorizarAcesso().setVisible(isAdminLogado);
        view.getBtnPromoverUsuario().setVisible(isAdminLogado);
        view.getBtnRebaixarUsuario().setVisible(isAdminLogado);
        view.getBtnRejeitarAcesso().setVisible(isAdminLogado);
    }
    
    private void irParaCadastro() {
       nav.abrirTela(new CadastroFactory(logger, false));
    }
    
    private void carregarTabelaUsuarios(boolean getAll) {
    
        String[] colunas = {"Nome", "Email", "Perfil"};

        List<Usuario> listaUsuarios = null;
        try {
            if(getAll) {
                listaUsuarios = service.getAllUsuarios();
                view.getLblTituloTabela().setText("Listagem de Usuários");
                habilitarAcoes(false);
            } else {
                listaUsuarios = service.getAllUsuariosNaoAutorizados();
                view.getLblTituloTabela().setText("Autorizações Pendentes");
                habilitarAcoes(true);
            }
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
    
    private void habilitarAcoes(boolean flag) {
        view.getBtnAutorizarAcesso().setEnabled(flag);
        view.getBtnPromoverUsuario().setEnabled(flag);
        view.getBtnRebaixarUsuario().setEnabled(flag);
        view.getBtnRejeitarAcesso().setEnabled(flag);
    }
        
}
