package br.com.controleacesso.service.impl;

import br.com.controleacesso.model.Notificacao;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.service.INotificacaoService;
import java.sql.SQLException;
import java.util.List;

public class NotificacaoServiceImpl implements INotificacaoService {
    
    private final INotificacaoRepository repository;

    public NotificacaoServiceImpl(INotificacaoRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<Notificacao> getNotificacoesNaoLidas(int idUsuario) throws SQLException {
        return repository.getNotificacoesNaoLidas(idUsuario);
    }
    
    @Override
    public void marcarComoLido(int idUsuario) throws SQLException {
        repository.marcarComoLido(idUsuario);
    }
    
}
