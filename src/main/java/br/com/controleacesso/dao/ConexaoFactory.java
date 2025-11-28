package br.com.controleacesso.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {
    
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:database.db");
    }
    
}
