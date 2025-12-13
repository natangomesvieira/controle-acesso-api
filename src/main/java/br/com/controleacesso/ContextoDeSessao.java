package br.com.controleacesso;

public class ContextoDeSessao {
    private final String perfilUsuarioLogado;
    private final String nomeUsuarioLogado;
    private final int idUsuarioLogado;

    public ContextoDeSessao(int id, String perfil, String nome) {
        this.idUsuarioLogado = id;
        this.perfilUsuarioLogado = perfil;
        this.nomeUsuarioLogado = nome;
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
    
    public String getNomeUsuarioLogado() {
        return nomeUsuarioLogado;
    }
    
}
