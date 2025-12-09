package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.LoginFactory;
import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.service.CadastroService;
import br.com.controleacesso.view.CadastroView;
import br.com.sistemalog.LogEntry;
import br.com.sistemalog.LogService;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class CadastroPresenter {
    
    private final CadastroView view;
    private final GerenciadorDeTelas nav;
    private final CadastroService service;
    private final LogService logger;
    private final boolean cadastroObrigatorio;

    public CadastroPresenter(CadastroView view, GerenciadorDeTelas nav, CadastroService service, LogService logger, boolean cadastroObrigatorio) {
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
        this.cadastroObrigatorio = cadastroObrigatorio;
        configuraAcessoAoPerfil();
        configuraView();
    }

    private void configuraView() {
        
        if (cadastroObrigatorio) { 
            view.getBtnCancelar().setEnabled(false); 
        }
        
        view.getBtnCadastrar().addActionListener((ActionEvent e) -> {
            cadastrar();
        });
        
        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            cancelar();
        });
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
        ContextoDeSessao sessao = nav.getSessao();
        
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }

        boolean mostrarPerfil = isAdminLogado && !cadastroObrigatorio;
        
        view.getLblPerfil().setVisible(mostrarPerfil);
        view.getCmbPerfil().setVisible(mostrarPerfil);
    }
    
    private void cadastrar() {
        
        Usuario usuario = new Usuario();
            
        try {
            
            usuario.setNome(view.getTxtNome().getText());
            usuario.setEmail(view.getTxtEmail().getText());
            usuario.setSenha(view.getPwdSenha().getText());
            usuario.setConfSenha(view.getPwdConfSenha().getText());
            
            String perfil = (String) view.getCmbPerfil().getSelectedItem();
            
            if(perfil != null && !perfil.isBlank()) {
                usuario.setPerfil(perfil);
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(view, 
                    "Confirma os dados para realizar o cadastro?", 
                    "Confirmar Cadastro", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                service.criarUsuario(usuario);

                ContextoDeSessao sessao = new ContextoDeSessao(usuario.getId(), usuario.getPerfil());

                nav.setSessao(sessao);

                logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome()));
                JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso!");  

                view.dispose();
                nav.abrirTela(new LoginFactory(logger));
            }
        } catch (Exception ex) {
            logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
    }
    
    private void cancelar() {
        view.dispose();
    }
    
}
