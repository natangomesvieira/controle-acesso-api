package br.com.controleacesso;

import br.com.controleacesso.repository.config.DatabaseConfig;
import br.com.controleacesso.presenter.HomePresenter;
import br.com.controleacesso.repository.UsuarioRepository;
import br.com.sistemalog.LogService;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ControleAcessoApi {
    
    public static void main(String[] args) {
        
        final LogService logger = new LogService("logs_do_sistema");
        
        //Conecta ao banco e cria as tabelas caso não exista
        new DatabaseConfig().inicializarBanco();

        // (Regra US 01)
        boolean sistemaPossuiUsuarios = false;
        try {
            UsuarioRepository repository = new UsuarioRepository();
            sistemaPossuiUsuarios = repository.temUsuariosCadastrados();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro crítico ao verificar banco de dados: " + e.getMessage());
            System.exit(1);
        }
        
        final boolean existeAdmin = sistemaPossuiUsuarios;
        
        // 2. Inicia o sistema
        SwingUtilities.invokeLater(() -> {
            HomePresenter mainPresenter = new HomePresenter(logger);
           
            if (existeAdmin) {
                mainPresenter.iniciarFluxo(true); 
            } else {
                // Cadastro Obrigatório do 1º Admin
                mainPresenter.iniciarFluxo(false);
            }
        });
        
    }
    
}
