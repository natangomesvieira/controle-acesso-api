package br.com.controleacesso;

import javax.swing.SwingUtilities;

public class ControleAcessoApi {
    
    public static void main(String[] args) {
        
        //Conecta ao banco e cria as tabelas caso não exista
        new DatabaseConfig().inicializarBanco();

        // 2. Inicia o sistema pelo Gerenciador
        SwingUtilities.invokeLater(() -> {
            GerenciadorDeTelas gerenciador = new GerenciadorDeTelas();
            gerenciador.telaHome();
        });
        
    }
    
}
