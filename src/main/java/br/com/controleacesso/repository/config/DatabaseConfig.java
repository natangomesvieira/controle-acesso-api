package br.com.controleacesso.repository.config;

import br.com.controleacesso.repository.config.ConexaoFactory;
import java.sql.SQLException;

public class DatabaseConfig {

    public void inicializarBanco() {
        createTables();
    }

    private void createTables() {

        var sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	nome TEXT NOT NULL,"
                + "	email TEXT NOT NULL UNIQUE,"
                + "	senha TEXT NOT NULL,"
                + "	perfil TEXT NOT NULL,"
                + "	autorizado BOOLEAN NOT NULL"
                + ");";

        var sqlNotificacao = "CREATE TABLE IF NOT EXISTS notificacao ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	id_usuario INTEGER NOT NULL,"
                + "	mensagem TEXT NOT NULL,"
                + "	data_hora DATETIME NOT NULL,"
                + "	lida BOOLEAN NOT NULL,"
                + "	FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE"
                + ");";

        try (var conn = ConexaoFactory.getConexao();
            var stmt = conn.createStatement()) {
            stmt.execute(sqlUsuario);
            stmt.execute(sqlNotificacao);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
    
}