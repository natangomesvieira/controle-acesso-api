package br.com.controleacesso.view.interfaces;

import br.com.controleacesso.presenter.CadastrarPresenter;

public interface ICadastrarView {
    
    String getEmail();
    String getSenha();

    void mostrarMensagem(String mensagem);

    void setPresenter(CadastrarPresenter presenter);
    
}
