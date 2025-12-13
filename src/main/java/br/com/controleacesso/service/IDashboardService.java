package br.com.controleacesso.service;

import br.com.controleacesso.model.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface IDashboardService {
    
    public List<Usuario> getAllUsuariosNaoAutorizados() throws SQLException;
    
    public List<Usuario> getAllUsuarios() throws SQLException;
    
    public Usuario getUsuarioById(int id) throws SQLException;
    
    public void autorizarAcessoByEmail(String email) throws SQLException;
    
    public void rejeitarAcessoByEmail(String email) throws SQLException;
    
    public void promoverUsuario(String email, String perfil, int idSolicitante) throws SQLException, IllegalAccessException;
    
    public void rebaixarUsuario(String email, String perfil, int idSolicitante) throws SQLException, IllegalAccessException;
    
    public void excluirUsuario(int idAlvo, int idSolicitante) throws SQLException;
    
    public void alterarSenha(int idUsuario, String senhaAtual, String novaSenha, String confSenha) throws Exception;
    
    public void restaurarSistema(int idAdmin, String senhaDigitada, String confSenhaDigitada) throws Exception;
    
}
