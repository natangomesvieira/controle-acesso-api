package br.com.controleacesso.view;

import br.com.controleacesso.factory.IViewFactory;
import br.com.controleacesso.factory.HomeFactory;
import br.com.controleacesso.view.HomeView;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class GerenciadorDeTelas {
    
    private JDesktopPane desktop;
    
    public void iniciarSistema() {
        HomeFactory factory = new HomeFactory();
        HomeView homeView = factory.criarTela(this);

        this.desktop = homeView.getDesktop();
        
        homeView.setLocationRelativeTo(null);
        homeView.setVisible(true);
    }

    public void abrirTela(IViewFactory factory) {
        if (this.desktop == null) {
            System.err.println("Erro: O sistema não foi iniciado corretamente.");
            return;
        }

        JInternalFrame frame = factory.criarTela(this);

        desktop.add(frame);
        centralizar(frame);
        frame.setVisible(true);
    }
    
    private void centralizar(JInternalFrame frame) {
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }   
    
}
