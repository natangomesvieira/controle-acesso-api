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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class DashboardPresenter {
    
    private final DashboardView view;
    private final GerenciadorDeTelas nav;
    private final LogService logger;
    private final DashboardService service;
    private boolean isListagemCompleta = true;
    private List<Usuario> usuariosEmExibicao;
    
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
        
        view.getTabelaUsuarios().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tratarSelecaoTabela();
                }
            }
        });
        
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
        view.getBtnRemoverUsuario().addActionListener((ActionEvent e) -> {
            excluirUsuario();
        });
        
        view.getBtnAlterarSenha().addActionListener((ActionEvent e) -> {
        abrirTelaAlterarSenha();
        });
    }
    
    private void tratarSelecaoTabela() {
        JTable tabela = view.getTabelaUsuarios();
        int linha = tabela.getSelectedRow();
        
        if (linha == -1) { desabilitarBotoesAcao(); return; }
        
        Usuario usuarioSelecionado = usuariosEmExibicao.get(linha);
        int idLogado = nav.getSessao().getIdUsuarioLogado();
        boolean souSuperAdmin = (idLogado == 1);

        if (isListagemCompleta) {
            
            boolean isSelecionadoRoot = Integer.valueOf(1).equals(usuarioSelecionado.getId());
            boolean isSelecionadoPadrao = "usuario_padrao".equals(usuarioSelecionado.getPerfil());

            view.getBtnPromoverUsuario().setEnabled(isSelecionadoPadrao);
            view.getBtnRebaixarUsuario().setEnabled(!isSelecionadoPadrao && !isSelecionadoRoot && souSuperAdmin);
            
            view.getBtnRemoverUsuario().setEnabled(souSuperAdmin && !isSelecionadoRoot);
            
            view.getBtnAutorizarAcesso().setEnabled(false);
            view.getBtnRejeitarAcesso().setEnabled(false);
            
        } else {
 
            view.getBtnAutorizarAcesso().setEnabled(true);
            view.getBtnRejeitarAcesso().setEnabled(true);
            
            view.getBtnPromoverUsuario().setEnabled(false);
            view.getBtnRebaixarUsuario().setEnabled(false);
            view.getBtnRemoverUsuario().setEnabled(souSuperAdmin);
        }
    }
    
    private void excluirUsuario() {
        try {
            JTable tabela = view.getTabelaUsuarios();
            int linha = tabela.getSelectedRow();
            if (linha == -1) return;

            Usuario usuarioAlvo = usuariosEmExibicao.get(linha);

            int confirmacao = JOptionPane.showConfirmDialog(view, 
                    "Tem certeza que deseja EXCLUIR o usuário " + usuarioAlvo.getNome() + "?\nEssa ação não pode ser desfeita.",
                    "Excluir Usuário",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                service.excluirUsuario(usuarioAlvo.getId());
                
               // logger.log(new LogEntry("EXCLUIR_USUARIO", usuarioAlvo.getEmail(), "SUCESSO", "Removido por Admin ID 1"));
                JOptionPane.showMessageDialog(view, "Usuário removido com sucesso!");
                
                carregarTabelaUsuarios(true);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Falha ao excluir: " + ex.getMessage());
        }
    }
    
    private void promoverUsuario() {
        try {
            JTable tabela = view.getTabelaUsuarios();
            int linha = tabela.getSelectedRow();
            Usuario usuarioAlvo = usuariosEmExibicao.get(linha);
    
            int linhaSelecionada = tabela.getSelectedRow();
    
            if (linhaSelecionada == -1) {
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tabela.getModel().getValueAt(linhaSelecionada, 1);
            String perfil = (String) tabela.getModel().getValueAt(linhaSelecionada, 2);
            
            if(email != null) {
<<<<<<< Updated upstream
                service.promoverUsuario(email, perfil);
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");
                carregarTabelaUsuarios(true);
=======
                int confirmacao = JOptionPane.showConfirmDialog(view, 
                        "Tem certeza que deseja PROMOVER o usuário " + usuarioAlvo.getNome() + " a Administrador?", 
                        "Confirmar Promoção", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    service.promoverUsuario(email, perfil);
                    //TODO:LOG
                    JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");
                    carregarTabelaUsuarios(false);
                }
>>>>>>> Stashed changes
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
            Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
            
            if (linhaSelecionada == -1) {
                //TODO:LOG
                JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tabela.getModel().getValueAt(linhaSelecionada, 1);
            String perfil = (String) tabela.getModel().getValueAt(linhaSelecionada, 2);
            
            if(email != null) {
                int confirmacao = JOptionPane.showConfirmDialog(view, 
                        "Tem certeza que deseja REBAIXAR o usuário " + usuarioAlvo.getNome() + " a Usuario Padrao?", 
                        "Confirmar Modificacao", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    service.rebaixarUsuario(email, perfil);
                    //TODO:LOG
                    JOptionPane.showMessageDialog(view, "Usuário rebaixado com sucesso!");
                    carregarTabelaUsuarios(true);
                }
           }
        } catch (Exception ex) {
            //TODO: LOG
            JOptionPane.showMessageDialog(view, "Falha ao rebaixar usuário!");
        }
    }
    
    private void autorizarAcesso() {
        try {
           JTable tabela = view.getTabelaUsuarios();
           int linhaSelecionada = tabela.getSelectedRow();
           Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);

           if(usuarioAlvo.getEmail() != null) {
                int confirmacao = JOptionPane.showConfirmDialog(view, 
                "Tem certeza que deseja AUTORIZAR o acesso do usuário " + usuarioAlvo.getNome() + " ao sistema?", 
                "Confirmar Promoção", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    service.autorizarAcessoByEmail(usuarioAlvo.getEmail());
                    //TODO:LOG
                    JOptionPane.showMessageDialog(view, "Usuário autorizado com sucesso!");
                    carregarTabelaUsuarios(true);
                }
           }
        } catch (Exception ex) {
            //TODO:LOG
            JOptionPane.showMessageDialog(view, "Falha ao realizar autorização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejeitarAcesso() {
        try {
           String email = emailUsuarioSelecionado();
           JTable tabela = view.getTabelaUsuarios();
           int linhaSelecionada = tabela.getSelectedRow();
           Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
           if(email != null) {
               
               int confirmacao = JOptionPane.showConfirmDialog(view, 
               "Tem certeza que deseja REJEITAR o acesso do o usuário?? " + usuarioAlvo.getNome() + " ao sistema?", 
               "Confirmar Promoção", 
               JOptionPane.YES_NO_OPTION, 
               JOptionPane.QUESTION_MESSAGE);
               
               if (confirmacao == JOptionPane.YES_OPTION) {
                    service.rejeitarAcessoByEmail(email);
                    //TODO:LOG
                    JOptionPane.showMessageDialog(view, "Usuário rejeitado com sucesso!");
                    carregarTabelaUsuarios(true);
               }
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
        
        this.isListagemCompleta = getAll;
    
        String[] colunas = {"Nome", "Email", "Perfil"};

        List<Usuario> listaUsuarios = null;
        try {
            if(getAll) {
                this.usuariosEmExibicao = service.getAllUsuarios(); 
                view.getLblTituloTabela().setText("Listagem de Usuários");
            } else {
                this.usuariosEmExibicao = service.getAllUsuariosNaoAutorizados();
                view.getLblTituloTabela().setText("Autorizações Pendentes");
            }
            desabilitarBotoesAcao();
            
        } catch (Exception e) {
            logger.log(new LogEntry("ERRO_LISTAGEM", e.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao carregar usuários: " + e.getMessage());
            return;
        }

        Object[][] dadosTabela = converterListaParaArray(this.usuariosEmExibicao);

        DefaultTableModel tableModel = new DefaultTableModel(dadosTabela, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        view.getTabelaUsuarios().setModel(tableModel);
    }
    
    private void desabilitarBotoesAcao() {
        view.getBtnAutorizarAcesso().setEnabled(false);
        view.getBtnPromoverUsuario().setEnabled(false);
        view.getBtnRebaixarUsuario().setEnabled(false);
        view.getBtnRejeitarAcesso().setEnabled(false);
        view.getBtnRemoverUsuario().setEnabled(false);
    }

    private Object[][] converterListaParaArray(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            return new Object[0][3];
        }

        Object[][] dados = new Object[usuarios.size()][3];

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            dados[i][0] = u.getNome();
            dados[i][1] = u.getEmail();
            dados[i][2] = u.getPerfil();
        }
        return dados;
    }
    
    private void abrirTelaAlterarSenha() {
    nav.abrirTela(new br.com.controleacesso.factory.AlterarSenhaFactory(logger));
}
        
}
