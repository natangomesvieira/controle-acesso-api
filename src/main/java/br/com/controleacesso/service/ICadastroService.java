package br.com.controleacesso.service;

import br.com.controleacesso.model.Usuario;

public interface ICadastroService {
    
    public void criarUsuario(Usuario usuario) throws Exception;
    
    public boolean cadastroInicial();
    
}
