package br.com.controleacesso;

import javax.swing.JInternalFrame;

public interface IViewFactory {
    JInternalFrame criarTela(GerenciadorDeTelas navegador);
}
