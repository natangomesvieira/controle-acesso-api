package br.com.controleacesso;

public class ContextoDeSessao {
    private final String perfilUsuarioLogado;
    private final int idUsuarioLogado;

    public ContextoDeSessao(int id, String perfil) {
        this.idUsuarioLogado = id;
        this.perfilUsuarioLogado = perfil;
    }

    public boolean isAdministrador() {
        return "Administrador".equalsIgnoreCase(perfilUsuarioLogado);
    }
    
    public String getPerfilUsuarioLogado() {
        return perfilUsuarioLogado;
    }
    
    public int getIdUsuarioLogado() {
        return idUsuarioLogado;
    }
}
