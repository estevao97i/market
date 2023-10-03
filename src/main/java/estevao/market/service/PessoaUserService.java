package estevao.market.service;

import estevao.market.model.PessoaJuridica;
import estevao.market.model.Usuario;
import estevao.market.repository.PessoaRepository;
import estevao.market.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;

@Service
@Transactional
@Component
@RequiredArgsConstructor
public class PessoaUserService {

    private final UsuarioRepository usuarioRepository;

    private final PessoaRepository pessoaRepository;

    private final JdbcTemplate jdbcTemplate;

    private final SendServiceEmail sendServiceEmail;

    public PessoaJuridica salvarPj(PessoaJuridica juridica) {

        for (int i = 0; i < juridica.getEnderecos().size(); i++) {
            juridica.getEnderecos().get(i).setPessoa(juridica);
            juridica.getEnderecos().get(i).setEmpresa(juridica);
        }

        juridica = pessoaRepository.save(juridica);

        Usuario usuarioPj = usuarioRepository.findByPessoa(juridica.getId(), juridica.getEmail());

        if (usuarioPj == null) {

            String constraint = usuarioRepository.consultaConstraintRole();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuario_acesso drop CONSTRAINT " + constraint + " ; commit ; ");
            }

            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPj.setEmpresa(juridica);
            usuarioPj.setPessoa(juridica);
            usuarioPj.setLogin(juridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
            usuarioPj.setSenha(senhaCriptografada);
            usuarioPj = usuarioRepository.save(usuarioPj);

            usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
            usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");

            StringBuilder messageHtml = new StringBuilder();

            messageHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            messageHtml.append("<b>Login: </b>"+juridica.getEmail()+"<br/>");
            messageHtml.append("<b>Senha </b>").append(senha).append("<br/>");
            messageHtml.append("<br/><br/> Obrigado!");

            try {
                sendServiceEmail.enviarEmailHtml("Acesso gerado para loja virtual", messageHtml.toString(), juridica.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return juridica;
    }




}
