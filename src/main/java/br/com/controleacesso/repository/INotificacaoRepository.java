package br.com.controleacesso.repository;

import br.com.controleacesso.model.Notificacao;
import java.sql.SQLException;
import java.util.List;

public interface INotificacaoRepository {
    
    public void salvar(Notificacao notificacao) throws SQLException;
    
    public List<Notificacao> getNotificacoesNaoLidas(int idUsuario) throws SQLException;
    
    public void marcarComoLido(int idUsuario) throws SQLException;
    
}
