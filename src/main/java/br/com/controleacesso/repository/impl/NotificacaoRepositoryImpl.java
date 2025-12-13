package br.com.controleacesso.repository.impl;

import br.com.controleacesso.model.Notificacao;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.config.ConexaoFactory;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoRepositoryImpl implements INotificacaoRepository {

    @Override
    public void salvar(Notificacao notificacao) throws SQLException {
        String sql = "INSERT INTO notificacao (id_usuario, mensagem, data_hora, lida) VALUES (?, ?, ?, ?)";
        
        try (var conn = ConexaoFactory.getConexao();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificacao.getIdUsuario()); 
            stmt.setString(2, notificacao.getMensagem());
            stmt.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(notificacao.getData()));
            stmt.setBoolean(4, notificacao.isLida()); 
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar notificação: " + e.getMessage());
        }
    }
    
    @Override
    public List<Notificacao> getNotificacoesNaoLidas(int idUsuario) throws SQLException {
        String sql = "SELECT id, mensagem, data_hora FROM notificacao WHERE id_usuario = ? AND lida = 0 ORDER BY data_hora DESC";
        List<Notificacao> notificacoes = new ArrayList<>();
        
        try (var conn = ConexaoFactory.getConexao();
             var stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notificacao notif = new Notificacao();
                    notif.setId(rs.getInt("id"));
                    notif.setIdUsuario(idUsuario);
                    notif.setMensagem(rs.getString("mensagem"));
                    notif.setData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("data_hora")));
                    notif.setLida(false);
                    
                    notificacoes.add(notif);
                }
            }
            
        } catch (SQLException | ParseException e) {
            throw new SQLException("Erro ao buscar notificações: " + e.getMessage());
        }
        
        return notificacoes;
    }
    
    @Override
    public void marcarComoLido(int idUsuario) throws SQLException {
        String sql = "UPDATE notificacao SET lida = 1 WHERE id_usuario = ?";

        try (var conn = ConexaoFactory.getConexao();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao marcar notificação como lida: " + e.getMessage());
        }
    }
    
}