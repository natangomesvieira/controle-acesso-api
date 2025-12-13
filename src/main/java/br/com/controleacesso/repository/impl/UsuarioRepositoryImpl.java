package br.com.controleacesso.repository.impl;

import br.com.controleacesso.repository.config.ConexaoFactory;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.IUsuarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements IUsuarioRepository {
    
    @Override
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
    
    @Override
    public List<Usuario> getAllUsuariosNaoAutorizados() throws SQLException {
    
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT nome, email, perfil FROM usuario WHERE autorizado = 0";

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
    
    @Override
    public List<Usuario> getAllUsuarios() throws SQLException {
    
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id, nome, email, perfil FROM usuario";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { 

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
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
    
    @Override
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, perfil, autorizado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());
            stmt.setBoolean(5, usuario.isAutorizado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int novoId = rs.getInt(1); 
                    usuario.setId(novoId);
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Erro ao salvar dados no banco: " + ex.getMessage()); 
        }
    }
    
    @Override
    public Usuario getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setAutorizado(rs.getBoolean("autorizado"));
            }
        }
        return usuario;
    }
    
    @Override
    public void autorizarAcessoByEmail(String email) throws SQLException {
        String sql = "UPDATE usuario SET autorizado = ? WHERE email = ?";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true); 
            stmt.setString(2, email); 
            stmt.executeUpdate(); 
        } catch (SQLException ex) {
            throw new SQLException("Erro ao atualizar o status de autorização do usuário: " + ex.getMessage());
        }
    }
    
    @Override
    public void rejeitarAcessoByEmail(String email) throws SQLException {
        String sql = "DELETE FROM usuario WHERE email = ?";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email); 
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erro ao deletar usuário por email: " + ex.getMessage());
        }
    }
    
    @Override
    public void alterarPefilUsuarioByEmail(String email, String perfil) throws SQLException {
        String sql = "UPDATE usuario SET perfil = ? WHERE email = ?";

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, perfil);
            stmt.setString(2, email); 
            stmt.executeUpdate(); 
        } catch (SQLException ex) {
            throw new SQLException("Erro ao atualizar o status de autorização do usuário: " + ex.getMessage());
        }
    }
    
    @Override
    public void deletarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        
        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            throw new SQLException("Erro ao excluir usuário: " + ex.getMessage());
        }
    }
    
    @Override
    public Usuario getById(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setAutorizado(rs.getBoolean("autorizado"));
            }
        }
        return usuario;
    }

    @Override
    public void atualizarSenha(int id, String novaSenha) throws SQLException {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
        
        try (Connection conn = ConexaoFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, novaSenha);
            stmt.setInt(2, id);
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erro ao atualizar senha: " + ex.getMessage());
        }
    }
    
    @Override
    public void resetarSistemaCompleto() throws SQLException {

        String sqlDeleteUsers = "DELETE FROM usuario";
        String sqlResetSequence = "DELETE FROM sqlite_sequence WHERE name = 'usuario'";
        
        try (Connection conn = ConexaoFactory.getConexao()) {
            conn.setAutoCommit(false);
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlDeleteUsers);
                stmt.executeUpdate(sqlResetSequence);
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    @Override
    public List<Integer> getAdminIds() throws SQLException {
        String sql = "SELECT id FROM usuario WHERE perfil = 'administrador'";
        List<Integer> adminIds = new ArrayList<>();
        
        try (var conn = ConexaoFactory.getConexao();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                adminIds.add(rs.getInt("id")); 
            }
            
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar IDs de administradores: " + e.getMessage());

        }
        
        return adminIds;
    }
}
