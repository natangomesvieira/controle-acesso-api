package br.com.controleacesso.presenter;

import br.com.controleacesso.view.GerenciadorDeTelas;
import br.com.controleacesso.factory.CadastroFactory;
import br.com.controleacesso.factory.LoginFactory;
import br.com.controleacesso.view.HomeView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

public class HomePresenter {
    
    private HomeView view;
    private GerenciadorDeTelas nav;
    private JDesktopPane desktop;
    
    public HomePresenter() {
        this.nav = new GerenciadorDeTelas();
        this.view = new HomeView();
        this.desktop = new JDesktopPane();
        configuraView();
    }
    
   private void configuraView() {
       view.setVisible(false);
       view.getBtnCadastrar().addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               try {
                   irParaCadastro();
               } catch (Exception ex) {
                   JOptionPane.showMessageDialog(view, "Falha:" + ex.getMessage());
               }
           }
       });
       
       view.getBtnEntrar().addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               try {
                   irParaLogin();
               } catch (Exception ex) {
                   JOptionPane.showMessageDialog(view, "Falha:" + ex.getMessage());
               }
          }
       });
       view.setVisible(true);
       view.setLocationRelativeTo(null);
       this.desktop = view.getDesktop();
   }
    
    private void irParaLogin() {
       nav.abrirTela(new LoginFactory(), desktop);
    }
    
    private void irParaCadastro() {
        nav.abrirTela(new CadastroFactory(), desktop);
    }
    
}
