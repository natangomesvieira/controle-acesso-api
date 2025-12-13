package br.com.controleacesso.service;

import br.com.controleacesso.model.Usuario;

public interface ILoginService {
    
    public Usuario login(Usuario usuario) throws Exception;
    
}
