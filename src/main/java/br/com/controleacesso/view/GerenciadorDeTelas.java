package br.com.controleacesso.view;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.IViewFactory;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class GerenciadorDeTelas {

    private final JDesktopPane desktop;
    private ContextoDeSessao sessao;

    public GerenciadorDeTelas(JDesktopPane desktop) {
        if (desktop == null) {
            throw new IllegalArgumentException("JDesktopPane não pode ser nulo.");
        }
        this.desktop = desktop;
    }

    public void abrirTela(IViewFactory factory) {
        
        JInternalFrame frame = factory.criarTela(this);
        
        desktop.add(frame);
        //frame.pack();
        frame.setSize(frame.getPreferredSize());
        
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setVisible(true);
        
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {}

    }
    
//    private void centralizarFrame(javax.swing.JInternalFrame frame) {
//        int x = (desktop.getWidth() - frame.getWidth()) / 2;
//        int y = (desktop.getHeight() - frame.getHeight()) / 2;
//    
//        if (x < 0) x = 0;
//        if (y < 0) y = 0;
//        
//        frame.setLocation(x, y);
//    }
    
    public void setSessao(ContextoDeSessao sessao) {
        this.sessao = sessao;
    } 
    
    public ContextoDeSessao getSessao() {
        return sessao;
    }
    
    public void limparSessao() {
        this.sessao = null;
    }
    
    public void fecharTodasAsTelas() {
        javax.swing.JInternalFrame[] frames = desktop.getAllFrames();
        
        for (javax.swing.JInternalFrame frame : frames) {
            frame.dispose(); 
        }
    }
    
}
