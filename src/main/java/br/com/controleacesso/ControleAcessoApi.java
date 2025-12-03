package br.com.controleacesso;

import br.com.controleacesso.repository.config.DatabaseConfig;
import br.com.controleacesso.presenter.HomePresenter;
import br.com.sistemalog.LogService;
import javax.swing.SwingUtilities;

public class ControleAcessoApi {
    
    public static void main(String[] args) {
        
        final LogService logger = new LogService("logs_do_sistema");
        
        //Conecta ao banco e cria as tabelas caso não exista
        new DatabaseConfig().inicializarBanco();

         // 2. Inicia o sistema
        SwingUtilities.invokeLater(() -> {
            new HomePresenter(logger);
        });
        
    }
    
}
