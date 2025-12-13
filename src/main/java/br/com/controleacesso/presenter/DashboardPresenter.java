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
import javax.swing.table.DefaultTableModel;

public class DashboardPresenter {
    
    private final DashboardView view;
    private final GerenciadorDeTelas nav;
    private final LogService logger;
    private final DashboardService service;
    private final ContextoDeSessao sessao;
    private boolean isListagemCompleta = true;
    private List<Usuario> usuariosEmExibicao;
    
    public DashboardPresenter(DashboardView view, GerenciadorDeTelas nav, DashboardService service, LogService logger, ContextoDeSessao sessao) {
        this.view = view;
        this.nav = nav;
        this.logger = logger;
        this.service = service;
        this.sessao = sessao;
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
    
    private void excluirUsuario() {

        JTable tabela = view.getTabelaUsuarios();
        int linha = tabela.getSelectedRow();
        
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAlvo = usuariosEmExibicao.get(linha);
        int idSolicitante = sessao.getIdUsuarioLogado();

        Optional.ofNullable(usuarioAlvo).ifPresent(u -> {
            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja EXCLUIR o usuário " + u.getNome() + "?\nEssa ação não pode ser desfeita.",
                    "Excluir Usuário",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            isAdmin(sessao.isAdministrador());
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    service.excluirUsuario(u.getId(), idSolicitante);

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
        int idSolicitante = sessao.getIdUsuarioLogado();

        Optional.ofNullable(usuarioAlvo.getEmail()).ifPresent(email -> {
            try {
                int confirmacao = JOptionPane.showConfirmDialog(view,
                        "Tem certeza que deseja PROMOVER o usuário " + usuarioAlvo.getNome() + " a Administrador?",
                        "Confirmar Promoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    
                    isAdmin(sessao.isAdministrador());
                    
                    service.promoverUsuario(email, usuarioAlvo.getPerfil(), idSolicitante);

                    logger.log(new LogEntry("PROMOVER_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));
                    JOptionPane.showMessageDialog(view, "Usuário promovido com sucesso!");

                    carregarTabelaUsuarios(true);
                }
           } catch (Exception ex) {
            logger.log(new LogEntry("PROMOVER_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
           }
        });
    }
    
    private void rebaixarUsuario() {
        JTable tabela = view.getTabelaUsuarios();
        int linhaSelecionada = tabela.getSelectedRow();
        Usuario usuarioAlvo = usuariosEmExibicao.get(linhaSelecionada);
        int idSolicitante = sessao.getIdUsuarioLogado();
            
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
                    
                    isAdmin(sessao.isAdministrador());
                    
                    service.rebaixarUsuario(email, perfil, idSolicitante);
                    
                    logger.log(new LogEntry("REBAIXAR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil()));

                    JOptionPane.showMessageDialog(view, "Usuário rebaixado com sucesso!");
                    carregarTabelaUsuarios(true);
                }
           }
        } catch (Exception ex) {
            logger.log(new LogEntry("REBAIXAR_USUARIO", usuarioAlvo.getNome(), usuarioAlvo.getPerfil(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
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
                    isAdmin(sessao.isAdministrador());
                    
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
                    isAdmin(sessao.isAdministrador());
                    
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
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }
        
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
       nav.abrirTela(new CadastroFactory(logger, false), sessao);
    }
    
    private void carregarTabelaUsuarios(boolean getAll) {

        this.isListagemCompleta = getAll;
        String[] colunas = {"Nome", "Email", "Perfil"};

        try {
            boolean isAdmin = sessao.isAdministrador();
            view.getTxtDescPerfil().setText(sessao.getPerfilUsuarioLogado());
            view.getTxtNomeUsuario().setText(sessao.getNomeUsuarioLogado());
            view.getTxtDescPerfil().setEditable(false);
            view.getTxtDescPerfil().setFocusable(false);
            view.getTxtNomeUsuario().setEditable(false);
            view.getTxtNomeUsuario().setFocusable(false);

            if (getAll) {
                
                view.getLblTituloTabela().setText("Listagem de Usuários");

                if (isAdmin) {
                    this.usuariosEmExibicao = service.getAllUsuarios();
                } else {
                    this.usuariosEmExibicao = new ArrayList<>();
                    Usuario eu = service.getUsuarioById(sessao.getIdUsuarioLogado());
                    if (eu != null) {
                        this.usuariosEmExibicao.add(eu);
                    }
                }

            } else {
                view.getLblTituloTabela().setText("Autorizações Pendentes");

                if (isAdmin) {
                    this.usuariosEmExibicao = service.getAllUsuariosNaoAutorizados();
                    logger.log(new LogEntry("LISTAGEM_USUARIOS", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));
                } else {
                    this.usuariosEmExibicao = new ArrayList<>();
                }
            }

        } catch (Exception e) {
            logger.log(new LogEntry("ERRO_LISTAGEM_USUARIOS", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), e.getMessage()));
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
        nav.abrirTela(new AlterarSenhaFactory(logger), sessao);
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
                isAdmin(sessao.isAdministrador());
                logger.setLogFormat(novoFormato);
                logger.log(new LogEntry("ALTERAR_FORMATO_LOG", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));
                JOptionPane.showMessageDialog(view, mensagemSucesso, "Configuração Salva", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                logger.log(new LogEntry("ALTERAR_FORMATO_LOG", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado(), ex.getMessage()));
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
            logger.log(new LogEntry("USUARIO_LOGOUT", sessao.getNomeUsuarioLogado(), sessao.getPerfilUsuarioLogado()));
            view.dispose();
            nav.abrirTela(new br.com.controleacesso.factory.LoginFactory(logger), null);
        }
    }
    
    private void abrirTelaRestauracao() {
         nav.abrirTela(new br.com.controleacesso.factory.RestaurarSistemaFactory(logger), sessao);
    }
    
    private void isAdmin(boolean isAdmin) {
        if(!isAdmin) {
            throw new RuntimeException("Privilégios insuficientes para realizar esta operação.");
        }
    }
    
}
