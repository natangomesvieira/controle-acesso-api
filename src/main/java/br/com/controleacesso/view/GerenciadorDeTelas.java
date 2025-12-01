package br.com.controleacesso.view;

import br.com.controleacesso.factory.IViewFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class GerenciadorDeTelas {

    public void abrirTela(IViewFactory factory, JDesktopPane desktop) {
        if (desktop == null) {
            System.err.println("Erro: O sistema não foi iniciado corretamente.");
            return;
        }

        JInternalFrame frame = factory.criarTela(this);

        desktop.add(frame);
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setVisible(true);
    }  
    
}
