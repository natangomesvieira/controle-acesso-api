package br.com.controleacesso;

import br.com.controleacesso.presenter.LoginPresenter;
import br.com.controleacesso.view.LoginView;
import javax.swing.SwingUtilities;

public class ControleAcessoApi {
    
    public static void main(String[] args) {
        
        //Conecta ao banco e cria as tabelas caso não exista
        DatabaseConfig db = new DatabaseConfig();
        db.inicializarBanco();

        // Inicia a tela principal
        SwingUtilities.invokeLater(() -> {  
            LoginView view = new LoginView();
            LoginPresenter presenter = new LoginPresenter(view);
            view.setPresenter(presenter);
            view.setLocationRelativeTo(null); 
            view.setVisible(true); 
        });
        
    }
    
}
