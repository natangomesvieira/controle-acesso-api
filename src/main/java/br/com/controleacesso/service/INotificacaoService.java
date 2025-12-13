package br.com.controleacesso.service;

import br.com.controleacesso.model.Notificacao;
import java.sql.SQLException;
import java.util.List;

public interface INotificacaoService {
    
    public List<Notificacao> getNotificacoesNaoLidas(int idUsuario) throws SQLException;
    
    public void marcarComoLido(int idUsuario) throws SQLException;
}
