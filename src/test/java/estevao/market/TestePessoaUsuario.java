package estevao.market;

import estevao.market.model.PessoaFisica;
import estevao.market.model.PessoaJuridica;
import estevao.market.repository.PessoaRepository;
import estevao.market.service.PessoaUserService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest(classes = MarketApplication.class)
public class TestePessoaUsuario extends TestCase {

    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testCadastraPessoa() {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj("684653132112");
        pessoaJuridica.setInscEstadual("1");
        pessoaJuridica.setInscMunicipal("2");
        pessoaJuridica.setNomeFantasia("23423423");
        pessoaJuridica.setRazaoSocial("23423423asd");
        pessoaJuridica.setNome("estevao");
        pessoaJuridica.setEmail("asdadsad@asdasda.com");
        pessoaJuridica.setTelefone("87797564687");

        pessoaRepository.save(pessoaJuridica);

//        PessoaFisica pessoaFisica = new PessoaFisica();
//        pessoaFisica.setCpf("684653132112");
//        pessoaFisica.setNome("estevao");
//        pessoaFisica.setEmail("asdadsad@asdasda.com");
//        pessoaFisica.setTelefone("87797564687");
//        pessoaFisica.setEmpresa(pessoaJuridica);

    }

}
