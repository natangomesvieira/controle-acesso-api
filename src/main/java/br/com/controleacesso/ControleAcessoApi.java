package br.com.controleacesso;

import br.com.controleacesso.factory.HomeFactory;
import br.com.controleacesso.repository.config.DatabaseConfig;
import br.com.sistemalog.LogService;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ControleAcessoApi {
    
    public static void main(String[] args) {
        
        final LogService logger = new LogService("logs_do_sistema");
        
        //Conecta ao banco e cria as tabelas caso não exista
        new DatabaseConfig().inicializarBanco();

         // 2. Inicia o sistema
        SwingUtilities.invokeLater(() -> {
            JFrame telaPrincipal = new HomeFactory(logger).criarTela();
            telaPrincipal.setVisible(true);
            telaPrincipal.setLocationRelativeTo(null);
        });
        
    }
    
}
