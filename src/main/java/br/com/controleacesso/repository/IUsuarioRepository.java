package br.com.controleacesso.repository;

import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface IUsuarioRepository {
    
    public boolean getAllUsers() throws SQLException;
    
    public List<Usuario> getAllUsuariosNaoAutorizados() throws SQLException;
    
    public List<Usuario> getAllUsuarios() throws SQLException;
    
    public void salvar(Usuario usuario) throws SQLException;
    
    public Usuario getByEmail(String email) throws SQLException;
    
    public void autorizarAcessoByEmail(String email) throws SQLException;
    
    public void rejeitarAcessoByEmail(String email) throws SQLException;
    
    public void alterarPefilUsuarioByEmail(String email, String perfil) throws SQLException;
    
    public void deletarUsuario(int id) throws SQLException;
    
    public Usuario getById(int id) throws SQLException;

    public void atualizarSenha(int id, String novaSenha) throws SQLException;
    
    public void resetarSistemaCompleto() throws SQLException;
    
    public List<Integer> getAdminIds() throws SQLException;
    
}
