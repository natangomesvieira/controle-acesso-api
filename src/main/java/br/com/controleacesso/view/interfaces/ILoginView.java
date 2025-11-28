package br.com.controleacesso.view.interfaces;

import br.com.controleacesso.presenter.LoginPresenter;

public interface ILoginView {
    
    public String getEmail();
    
    public String getSenha();
    
    public void setPresenter(LoginPresenter presenter);
    
    public void mostrarMensagem(String mensagem);
    
}
