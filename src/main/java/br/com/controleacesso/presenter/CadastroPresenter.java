package br.com.controleacesso.presenter;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.DashboardFactory;
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
<<<<<<< Updated upstream
    private final boolean cadastroObrigatorio;
    private final ContextoDeSessao sessao;

    public CadastroPresenter(CadastroView view, GerenciadorDeTelas nav, CadastroService service, LogService logger, ContextoDeSessao sessao, boolean cadastroObrigatorio) {
=======
    private final ContextoDeSessao sessao;

    public CadastroPresenter(CadastroView view, GerenciadorDeTelas nav, CadastroService service, LogService logger, ContextoDeSessao sessao) {
>>>>>>> Stashed changes
        this.view = view;
        this.nav = nav;
        this.service = service;
        this.logger = logger;
<<<<<<< Updated upstream
        this.cadastroObrigatorio = cadastroObrigatorio;
=======
>>>>>>> Stashed changes
        this.sessao = sessao;
        configuraAcessoAoPerfil();
        configuraView();
    }

    private void configuraView() {
<<<<<<< Updated upstream
        
        if (cadastroObrigatorio) { view.getBtnCancelar().setEnabled(false); } // OU view.getBtnCancelar().setVisible(false); 
        
=======
>>>>>>> Stashed changes
        view.getBtnCadastrar().addActionListener((ActionEvent e) -> {
            cadastrar();
        });
        
        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            cancelar();
        });
    }
    
    private void configuraAcessoAoPerfil() {
        boolean isAdminLogado = false;
    
        if (sessao != null) {
            isAdminLogado = sessao.isAdministrador();
        }

<<<<<<< Updated upstream
        boolean mostrarPerfil = isAdminLogado && !cadastroObrigatorio;
        
        view.getPerfil().setVisible(mostrarPerfil); 
=======
        view.getPerfil().setVisible(isAdminLogado); 
>>>>>>> Stashed changes
    }
    
    private void cadastrar() {
        
        Usuario usuario = new Usuario();
            
        try {
            
            usuario.setNome(view.getTxtNome().getText());
            usuario.setEmail(view.getTxtEmail().getText());
            usuario.setSenha(view.getPwdSenha().getText());
            usuario.setConfSenha(view.getPwdConfSenha().getText());
            
            String perfil = (String) view.getPerfil().getSelectedItem();
            
            if(perfil != null && !perfil.isBlank()) {
                usuario.setPerfil(perfil);
            }

            service.criarUsuario(usuario);
            
            logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome()));
            
            nav.abrirTela(new DashboardFactory(logger));
        } catch (Exception ex) {
            logger.log(new LogEntry("CADASTRO_USUARIO", usuario.getNome(), ex.getMessage()));
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }

    }
    
    private void cancelar() {
        view.dispose();
    }
    
}
