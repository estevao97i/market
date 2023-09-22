package estevao.market.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import estevao.market.model.PessoaJuridica;
import estevao.market.model.Usuario;
import estevao.market.repository.PessoaRepository;
import estevao.market.repository.UsuarioRepository;
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
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        }
        return juridica;
    }


}
