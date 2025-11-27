package br.com.controleacesso;

import br.com.controleacesso.presenter.HomePresenter;
import br.com.controleacesso.presenter.LoginPresenter;
import br.com.controleacesso.view.HomeView;
import br.com.controleacesso.view.LoginView;
import javax.swing.JFrame;

public class GerenciadorDeTelas {
    
    //Método para abrir a tela home
    public void telaHome() {
        HomeView view = new HomeView();
        HomePresenter presenter = new HomePresenter(view, this);
        view.setPresenter(presenter);
        
        configurarEExibir(view);
    }
    
    //Método para abrir tela de cadastrar usuário
    public void criarLogin() {
        LoginView view = new LoginView();
        LoginPresenter presenter = new LoginPresenter(view, this);
        view.setPresenter(presenter);
        
        configurarEExibir(view);
    }
    
    public void telaCadastro() {
        LoginView view = new LoginView();
        LoginPresenter presenter = new LoginPresenter(view, this);
        view.setPresenter(presenter);
        
        configurarEExibir(view);
    }

    // Método para abrir a Tela Principal (Dashboard)
/*    public void abrirPrincipal(String nomeUsuario) {
        PrincipalView view = new PrincipalView();
        PrincipalPresenter presenter = new PrincipalPresenter(view, nomeUsuario);
        view.setPresenter(presenter);
        
        configurarEExibir(view);
    }*/
    
    // Método auxiliar para não repetir código de configuração
    private void configurarEExibir(JFrame janela) {
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
    
}
