package br.com.controleacesso.view;

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
    
    public void abrirTela(IViewFactory factory) {
        
        JInternalFrame frame = factory.criarTela(this);
        
        desktop.add(frame);
        frame.pack();
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setVisible(true);
        
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {}
    }  
    
}
