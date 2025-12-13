package br.com.controleacesso.service.impl;

import br.com.controleacesso.model.Notificacao;
import br.com.controleacesso.model.Usuario;
import br.com.controleacesso.repository.INotificacaoRepository;
import br.com.controleacesso.repository.IUsuarioRepository;
import br.com.controleacesso.service.ICadastroService;
import com.pss.senha.validacao.ValidadorSenha;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CadastroServiceImpl implements ICadastroService {
    
    private final IUsuarioRepository usuarioRepository;
    private final ValidadorSenha validadorSenha;
    private final INotificacaoRepository notificacaoRepository;
    
    public CadastroServiceImpl(IUsuarioRepository usuarioRepository, INotificacaoRepository notificacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.validadorSenha = new ValidadorSenha();
    }
    
    @Override
    public boolean cadastroInicial() {
        try {
            return !usuarioRepository.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar estado inicial do banco de dados: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void criarUsuario(Usuario usuario) throws Exception {

        validarCamposObrigatorios(usuario);

        List<String> erros = validadorSenha.validar(usuario.getSenha());

        Optional.ofNullable(erros)
                .filter(lista -> !lista.isEmpty())
                .ifPresent(lista -> {
                    throw new IllegalArgumentException("Senha fraca:\n- " + String.join("\n- ", lista));
                });

        try {
            boolean existeUsuario = usuarioRepository.getAllUsers();

            String perfil = verificarPerfil(usuario.getPerfil(), existeUsuario);
            usuario.setPerfil(perfil);

            boolean isAdmin = "administrador".equals(perfil);
            usuario.setAutorizado(isAdmin);

            usuarioRepository.salvar(usuario);
            adicionarNotificacaoParaAdmins(usuario);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar usuários: " + e.getMessage(), e);
        }
    }
    
    public String verificarPerfil(String perfil, boolean existeUsuario) {

        return Optional.ofNullable(perfil)
            .filter(Predicate.not(String::isBlank))
            .orElse(existeUsuario ? "usuario_padrao" : "administrador");
    }
    
    private void adicionarNotificacaoParaAdmins(Usuario usuarioCriado) throws SQLException {
        
        if ("usuario_padrao".equalsIgnoreCase(usuarioCriado.getPerfil())) {
            
            List<Integer> adminIds = usuarioRepository.getAdminIds(); 
            
            String mensagem = "Novo usuário '" + usuarioCriado.getNome() + "' criado e pendente de aprovação.";

            for (int adminId : adminIds) {
                Notificacao notificacao = new Notificacao();
                notificacao.setIdUsuario(adminId);
                notificacao.setLida(false);
                notificacao.setData(new Date());
                notificacao.setMensagem(mensagem);
                
                notificacaoRepository.salvar(notificacao);
            }
        }
    }
    
    private void validarCamposObrigatorios(Usuario usuario) {
        
        validarEmail(usuario.getEmail());

        Optional.ofNullable(usuario)
            .orElseThrow(() -> new IllegalArgumentException("O objeto usuário não pode ser nulo!"));

        Optional.ofNullable(usuario.getNome())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo Nome precisa ser preenchido!"));

        Optional.ofNullable(usuario.getEmail())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo E-mail precisa ser preenchido!"));

        Optional.ofNullable(usuario.getSenha())
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("O campo Senha precisa ser preenchido!"));

        Optional.ofNullable(usuario.getConfSenha())
        .filter(conf -> conf.equals(usuario.getSenha()))
        .orElseThrow(() -> new IllegalArgumentException("A confirmação da senha não confere!"));
    }
    
    private void validarEmail(String email) {    
        String regex = "^[\\w\\.-]+@[\\w\\.-]+\\.(com|com\\.br)$";

        if (!email.toLowerCase().matches(regex)) {
            throw new IllegalArgumentException("E-mail inválido! Verifique o formato (ex: nome@dominio.com).");
        }
    }
}