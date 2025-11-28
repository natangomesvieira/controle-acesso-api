package br.com.controleacesso.dao;

import br.com.controleacesso.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            
            stmt.executeUpdate();
        }
    }
    
    public Usuario getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
            }
        }
        return usuario;
    }
    
}
