package br.com.controleacesso.factory;

import br.com.controleacesso.ContextoDeSessao;
import br.com.controleacesso.view.GerenciadorDeTelas;
import javax.swing.JInternalFrame;

public interface IViewFactory {
    JInternalFrame criarTela(GerenciadorDeTelas nav, ContextoDeSessao sessao);
}
