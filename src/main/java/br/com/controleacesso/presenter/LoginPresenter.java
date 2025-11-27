package br.com.controleacesso.presenter;

import br.com.controleacesso.view.ILoginView;

public class LoginPresenter {
    
    private ILoginView view;

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    public void autenticar() {
        String email = view.getEmail();
        String senha = view.getSenha();

        if (email.equals("admin@email.com") && senha.equals("123")) {
            view.mostrarMensagem("Login com Sucesso!");
            view.fecharTela();
        } else {
            view.mostrarMensagem("Usuário ou senha inválidos!");
        }
    }
    
}
