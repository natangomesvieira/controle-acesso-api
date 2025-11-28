package br.com.controleacesso.dao;

import br.com.controleacesso.dao.ConexaoFactory;
import java.sql.SQLException;

public class DatabaseConfig {

    public void inicializarBanco() {
        createTables();
    }

    private void createTables() {
        var sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	nome TEXT NOT NULL,"
                + "	email TEXT NOT NULL UNIQUE,"
                + "     senha TEXT NOT NULL"
                + ");";

        try (var conn = ConexaoFactory.getConexao();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'usuario' verificada/criada.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
    
}