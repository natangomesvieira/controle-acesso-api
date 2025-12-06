package br.com.controleacesso.repository;

import br.com.controleacesso.repository.config.ConexaoFactory;
import br.com.controleacesso.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    
    public boolean getAllUsers() throws SQLException {
        String sql = "SELECT count(1) as total FROM usuario";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total") > 0;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao realizar consulta no banco de dados!");
        }
    }
    
    public List<Usuario> getAllUsuarios() throws SQLException {
    
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT nome, email, perfil FROM usuario";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { 

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPerfil(rs.getString("perfil"));

                usuarios.add(usuario);
            }
            return usuarios; 

        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar todos os usuários no banco de dados: " + ex.getMessage());
        }
    }
    
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erro ao salvar dados no banco!");
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
                usuario.setPerfil(rs.getString("perfil"));
            }
        }
        return usuario;
    }
    
}
