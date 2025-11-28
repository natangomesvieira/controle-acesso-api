package br.com.controleacesso.view.interfaces;

import br.com.controleacesso.presenter.CadastroPresenter;

public interface ICadastroView {
    
    public String getNome();
    
    public String getEmail();
    
    public String getSenha();
    
    public String getConfSenha();

    public void mostrarMensagem(String mensagem);

    public void setPresenter(CadastroPresenter presenter);
    
}
