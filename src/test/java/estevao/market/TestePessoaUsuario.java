package estevao.market;

import estevao.market.controller.PessoaController;
import estevao.market.exception.MarketException;
import estevao.market.model.PessoaFisica;
import estevao.market.model.PessoaJuridica;
import estevao.market.repository.PessoaRepository;
import estevao.market.service.PessoaUserService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = MarketApplication.class)
public class TestePessoaUsuario extends TestCase {

    @Autowired
    private PessoaController pessoaController;

    @Test
    public void testCadastraPessoa() throws MarketException {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setInscEstadual("1");
        pessoaJuridica.setInscMunicipal("2");
        pessoaJuridica.setNomeFantasia("23423423");
        pessoaJuridica.setRazaoSocial("23423423asd");
        pessoaJuridica.setNome("estevao");
        pessoaJuridica.setEmail("asdadsad@asdasda.com");
        pessoaJuridica.setTelefone("87797564687");

        pessoaController.salvarPj(pessoaJuridica);

    }

}
