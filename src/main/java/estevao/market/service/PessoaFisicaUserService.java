package estevao.market.service;

import estevao.market.model.PessoaFisica;
import estevao.market.model.Usuario;
import estevao.market.repository.PessoaFisicaRepository;
import estevao.market.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Transactional
public class PessoaFisicaUserService {

    private final UsuarioRepository usuarioRepository;

    private final PessoaFisicaRepository pessoaRepository;

    private final JdbcTemplate jdbcTemplate;

    private final SendServiceEmail sendServiceEmail;

    public PessoaFisica salvarPf(PessoaFisica fisica) {

        for (int i = 0; i < fisica.getEnderecos().size(); i++) {
            fisica.getEnderecos().get(i).setPessoa(fisica);
            fisica.getEnderecos().get(i).setEmpresa(fisica);
        }

        fisica = pessoaRepository.save(fisica);

        Usuario usuarioPf = usuarioRepository.findByPessoa(fisica.getId(), fisica.getEmail());

        if (usuarioPf == null) {

            String constraint = usuarioRepository.consultaConstraintRole();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuario_acesso drop CONSTRAINT " + constraint + " ; commit ; ");
            }

            usuarioPf = new Usuario();
            usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPf.setEmpresa(fisica);
            usuarioPf.setPessoa(fisica);
            usuarioPf.setLogin(fisica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
            usuarioPf.setSenha(senhaCriptografada);
            usuarioPf = usuarioRepository.save(usuarioPf);

            usuarioRepository.insereAcessoUserPj(usuarioPf.getId());

            StringBuilder messageHtml = new StringBuilder();

            messageHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            messageHtml.append("<b>Login: </b>" + fisica.getEmail() + "<br/>");
            messageHtml.append("<b>Senha </b>").append(senha).append("<br/>");
            messageHtml.append("<br/><br/> Obrigado!");

            try {
                sendServiceEmail.enviarEmailHtml("Acesso gerado para loja virtual", messageHtml.toString(), fisica.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fisica;
    }


}
