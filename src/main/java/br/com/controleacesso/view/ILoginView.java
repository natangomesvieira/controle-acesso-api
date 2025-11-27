package br.com.controleacesso.view;

import br.com.controleacesso.presenter.LoginPresenter;

public interface ILoginView {
    
    String getEmail();
    String getSenha();

    void mostrarMensagem(String mensagem);
    
    void fecharTela();

    void setPresenter(LoginPresenter presenter);
    
}
