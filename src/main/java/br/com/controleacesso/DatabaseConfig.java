package br.com.controleacesso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    
    private final String url = "jdbc:sqlite:database.db";

    public void inicializarBanco() {
        createTables();
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTables() {
        var sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	nome TEXT NOT NULL,"
                + "	email TEXT NOT NULL UNIQUE,"
                + "     senha TEXT NOT NULL"
                + ");";

        try (var conn = conectar();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'usuario' verificada/criada.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    private void insertData() {
        if (verificarSeTemDados()) return;

        var nomes = new String[] {"Joana Silva", "Luis Souza", "Gildo Costa"};
        var emails = new String[] {"joana@gmail.com", "luis@gmail.com", "gildo@gmail.com"};
        var senhas = new String[] {"Joana123", "Luis123", "Gildo123"};
        
        String sql = "INSERT INTO usuario(nome, email, senha) VALUES(?,?,?)";
        
        try (var conn = conectar();
             var pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < nomes.length; i++) {
                pstmt.setString(1, nomes[i]);
                pstmt.setString(2, emails[i]);
                pstmt.setString(3, senhas[i]);
                pstmt.executeUpdate();
            }
            System.out.println("Dados iniciais inseridos com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
        }
    }
    
    public void listarUsuarios() {
        String sql = "SELECT id, nome, email, senha FROM usuario";

        System.out.println("\n--- LISTA DE USUÁRIOS ---");

        try (var conn = conectar();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                System.out.printf("ID: %d | Nome: %-15s | Email: %-15s | Senha: %s%n", id, nome, email, senha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar: " + e.getMessage());
        }
    }
    
    private boolean verificarSeTemDados() {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (var conn = conectar();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar existencia de dadoss");
        }
        return false;
    }

    public void apagarTabela() {
        String sql = "DROP TABLE IF EXISTS usuario";

        try (var conn = conectar();
             var stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Tabela 'usuario' foi destruída/apagada.");
            
        } catch (SQLException e) {
            System.err.println("Erro ao apagar tabela: " + e.getMessage());
        }
    }
    
}
