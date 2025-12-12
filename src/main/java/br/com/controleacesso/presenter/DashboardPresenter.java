package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.AlterarSenhaFactory;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.DashboardService;
import br.com.controleacesso.view.DashboardView;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogFormat;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        
        view.getBtnConfiguracaoLog().addActionListener((ActionEvent e) -> {
            formatoLog();
        });
        
        view.getBtnSair().addActionListener((ActionEvent e) -> {
            fazerLogout();
        });
        
        view.getBtnRestaurarSistema().addActionListener(e -> {
            abrirTelaRestauracao();
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

        JTable tabela = view.getTabelaUsuarios();
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAlvo = usuariosEmExibicao.get(linha);

        Optional.ofNullable(usuarioAlvo).ifPresent(u -> {
            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja EXCLUIR o usuário " + u.getNome() + "?\nEssa ação não pode ser desfeita.",
                    "Excluir Usuário",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    service.excluirUsuario(u.getId());

                    logger.log(new LogEntry("EXCLUIR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));
                    JOptionPane.showMessageDialog(view, "Usuário removido com sucesso!");

                    carregarTabelaUsuarios(true);
                } catch (Exception ex) {
                    logger.log(new LogEntry("EXCLUIR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
                    JOptionPane.showMessageDialog(view, "Falha ao excluir: " + ex.getMessage());
                }
            }
        });
    }
    
    private void promoverUsuario() {

        JTable tabela = view.getTabelaUsuarios();
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAlvo = usuariosEmExibicao.get(linha);

        Optional.ofNullable(usuarioAlvo.getEmail()).ifPresent(email -> {
            try {
                int confirmacao = JOptionPane.showConfirmDialog(view,
                        "Tem certeza que deseja PROMOVER o usuário " + usuarioAlvo.getNome() + " a Administrador?",
                        "Confirmar Promoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    service.promoverUsuario(email, usuarioAlvo.getPerfil());

                    logger.log(new LogEntry("PROMOVER_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));
                    JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");

                    carregarTabelaUsuarios(true);
                }
           } catch (Exception ex) {
            logger.log(new LogEntry("PROMOVER_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao promover usuário!");
           }
        });
    }
    
    private void rebaixarUsuario() {
        JTable tabela = view.getTabelaUsuarios();
        int linhaSelecionada = tabela.getSelectedRow();
        Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
            
        try {
            if (linhaSelecionada == -1) {
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
                    
                    logger.log(new LogEntry("REBAIXAR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));

                    JOptionPane.showMessageDialog(view, "Usuário rebaixado com sucesso!");
                    carregarTabelaUsuarios(true);
                }
           }
        } catch (Exception ex) {
            logger.log(new LogEntry("REBAIXAR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, "Falha ao rebaixar usuário!");
        }
    }
    
    private void autorizarAcesso() {
        JTable tabela = view.getTabelaUsuarios();
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
        
        Optional.ofNullable(usuarioAlvo).ifPresent(u -> {

            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja AUTORIZAR o acesso do usuário " + u.getNome() + " ao sistema?",
                    "Confirmar Promoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    service.autorizarAcessoByEmail(u.getEmail());

                    logger.log(new LogEntry("AUTORIZAR_ACESSO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));
                    JOptionPane.showMessageDialog(view, "Usuário autorizado com sucesso!");

                    carregarTabelaUsuarios(true);
                } catch (Exception ex) {
                    logger.log(new LogEntry("AUTORIZAR_ACESSO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
                    JOptionPane.showMessageDialog(view, "Falha ao realizar autorização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void rejeitarAcesso() {
        JTable tabela = view.getTabelaUsuarios();
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
        Optional.ofNullable(usuarioAlvo).ifPresent(u -> {

            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja REJEITAR o acesso do o usuário?? " + u.getNome() + " ao sistema?",
                    "Confirmar Rejeição",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    service.rejeitarAcessoByEmail(u.getEmail());

                    logger.log(new LogEntry("REJEITAR_ACESSO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));
                    JOptionPane.showMessageDialog(view, "Usuário rejeitado com sucesso!");

                    carregarTabelaUsuarios(true);
                } catch (Exception ex) {
                    logger.log(new LogEntry("REJEITAR_ACESSO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
                    JOptionPane.showMessageDialog(view, "Falha ao realizar rejeição: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
        ContextoDeSessao sessao = nav.getSessao();
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }
        
        //view.getBtnListagemUsuarios().setVisible(isAdminLogado);
        view.getBtnNovoUsuario().setVisible(isAdminLogado); 
        view.getBtnAutorizarAcesso().setVisible(isAdminLogado);
        view.getBtnPromoverUsuario().setVisible(isAdminLogado);
        view.getBtnRebaixarUsuario().setVisible(isAdminLogado);
        view.getBtnRejeitarAcesso().setVisible(isAdminLogado);
        view.getBtnRemoverUsuario().setVisible(isAdminLogado);
        view.getBtnConfiguracaoLog().setVisible(isAdminLogado);
        view.getBtnAutorizacoesPendentes().setVisible(isAdminLogado);
        view.getBtnListagemUsuarios().setVisible(true);
        view.getBtnAlterarSenha().setVisible(true);
        //label
        view.getLblAcoesGlobais().setVisible(isAdminLogado);
        view.getLblAcoesSelecao().setVisible(isAdminLogado);
        
        view.getBtnRestaurarSistema().setVisible(isAdminLogado);
    }
    
    private void irParaCadastro() {
       nav.abrirTela(new CadastroFactory(logger, false));
    }
    
    private void carregarTabelaUsuarios(boolean getAll) {

        this.isListagemCompleta = getAll;
        String[] colunas = {"Nome", "Email", "Perfil"};

        try {
            boolean isAdmin = nav.getSessao().isAdministrador();
            int idLogado = nav.getSessao().getIdUsuarioLogado();

            if (getAll) {
                
                view.getLblTituloTabela().setText("Listagem de Usuários");

                if (isAdmin) {
                    this.usuariosEmExibicao = service.getAllUsuarios();
                } else {
                    this.usuariosEmExibicao = new ArrayList<>();
                    Usuario eu = service.getUsuarioById(idLogado);
                    if (eu != null) {
                        this.usuariosEmExibicao.add(eu);
                    }
                }

            } else {
                view.getLblTituloTabela().setText("Autorizações Pendentes");

                if (isAdmin) {
                    this.usuariosEmExibicao = service.getAllUsuariosNaoAutorizados();
                    logger.log(new LogEntry("LISTAGEM_USUARIOS", "-", nav.getSessao().getPerfilUsuarioLogado()));
                } else {
                    this.usuariosEmExibicao = new ArrayList<>();
                }
            }
            desabilitarBotoesAcao();

        } catch (Exception e) {
            logger.log(new LogEntry("ERRO_LISTAGEM_USUARIOS", "-", nav.getSessao().getPerfilUsuarioLogado(), e.getMessage()));
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
        nav.abrirTela(new AlterarSenhaFactory(logger));
    }
    
    private void formatoLog() {
        Object[] options = {
            LogFormat.CSV.name(), 
            LogFormat.JSONL.name(), 
            "Cancelar"
        };
        
        int escolha = JOptionPane.showOptionDialog(
            view,
            "Selecione o formato de log desejado (CSV ou JSONL).\n\nEsta escolha será persistida.", 
            "Configuração de Log",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        LogFormat novoFormato = null;
        String mensagemSucesso = "Formato de log alterado com sucesso para ";
        
        if (escolha == JOptionPane.CLOSED_OPTION || escolha == 2) {
            return;
        } else if (escolha == 0) {
            novoFormato = LogFormat.CSV;
            mensagemSucesso += "CSV.";
        } else if (escolha == 1) {
            novoFormato = LogFormat.JSONL;
            mensagemSucesso += "JSONL.";
        }

        if (novoFormato != null) {
            try {
                logger.setLogFormat(novoFormato); 
                JOptionPane.showMessageDialog(view, mensagemSucesso, "Configuração Salva", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    "Erro ao salvar a configuração de log: " + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void fazerLogout() {
        int confirmacao = JOptionPane.showConfirmDialog(view, 
                "Deseja realmente sair do sistema?", 
                "Sair", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            nav.limparSessao();
            view.dispose();
            nav.abrirTela(new br.com.controleacesso.factory.LoginFactory(logger));
        }
    }
    
    private void abrirTelaRestauracao() {
         nav.abrirTela(new br.com.controleacesso.factory.RestaurarSistemaFactory(logger));
    }
        
}
