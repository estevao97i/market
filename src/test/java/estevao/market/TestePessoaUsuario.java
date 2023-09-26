package estevao.market;

import estevao.market.controller.PessoaController;
import estevao.market.enums.TipoEndereco;
import estevao.market.exception.MarketException;
import estevao.market.model.Endereco;
import estevao.market.model.PessoaJuridica;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Arrays;
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
        pessoaJuridica.setNome("estevao2");
        pessoaJuridica.setEmail("testeSalvasertdsarPJ@gmail.com");
        pessoaJuridica.setTelefone("87797564687");

        Endereco endereco1 = new Endereco();
        endereco1.setRuaLogra("123");
        endereco1.setCep("123123123");
        endereco1.setNumero("333222");
        endereco1.setBairro("centro");
        endereco1.setUf("DF");
        endereco1.setCidade("taguabas");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);

        Endereco endereco2 = new Endereco();
        endereco2.setRuaLogra("1234");
        endereco2.setCep("1231231234");
        endereco2.setNumero("3332224");
        endereco2.setBairro("centro 1");
        endereco2.setUf("DF1");
        endereco2.setCidade("taguabas1");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);

        pessoaJuridica.getEnderecos().add(endereco1);
        pessoaJuridica.getEnderecos().add(endereco2);

        pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();

        assert pessoaJuridica != null;
        assertTrue(pessoaJuridica.getId() > 0);

        for (Endereco endereco: pessoaJuridica.getEnderecos()) {
            assertTrue(endereco.getId() > 0);
        }

        assertEquals(2, pessoaJuridica.getEnderecos().size());

    }

}
