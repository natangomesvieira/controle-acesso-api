package br.com.controleacesso.view;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.factory.IViewFactory;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class GerenciadorDeTelas {

    private final JDesktopPane desktop;

    public GerenciadorDeTelas(JDesktopPane desktop) {
        if (desktop == null) {
            throw new IllegalArgumentException("JDesktopPane não pode ser nulo.");
        }
        this.desktop = desktop;
    }

    public void abrirTela(IViewFactory factory, ContextoDeSessao sessao) {
        
        JInternalFrame frame = factory.criarTela(this, sessao);
        
        desktop.add(frame);
        centralizarFrame(frame);
        frame.setVisible(true);
        
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {}

    }

    private void centralizarFrame(javax.swing.JInternalFrame frame) {
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
    
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        
        frame.setLocation(x, y);
    }
    
    public void fecharTodasAsTelas() {
        javax.swing.JInternalFrame[] frames = desktop.getAllFrames();
        
        for (javax.swing.JInternalFrame frame : frames) {
            frame.dispose(); 
        }
    }
    
}
