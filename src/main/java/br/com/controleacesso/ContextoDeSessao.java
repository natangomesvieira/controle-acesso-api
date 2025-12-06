package br.com.controleacesso;

public class ContextoDeSessao {
    private final String perfilUsuarioLogado;

    public ContextoDeSessao(String perfil) {
        this.perfilUsuarioLogado = perfil;
    }

    public boolean isAdministrador() {
        return "Administrador".equalsIgnoreCase(perfilUsuarioLogado);
    }
    
    public String getPerfilUsuarioLogado() {
        return perfilUsuarioLogado;
    }
}
