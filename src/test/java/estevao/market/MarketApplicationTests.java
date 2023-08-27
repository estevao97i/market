package estevao.market;

import estevao.market.controller.AcessoController;
import estevao.market.model.Acesso;
import estevao.market.repository.AcessoRepository;
import estevao.market.service.AcessoService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MarketApplication.class)
class MarketApplicationTests extends TestCase {

	@Autowired
	private AcessoController controller;

	@Test
	public void tentaCadastrarAcesso() {
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");

		var acessoSalvo = controller.salvar(acesso).getBody();

		assertTrue(acessoSalvo.getId() > 0);
		assertEquals("ROLE_ADMIN", acessoSalvo.getDescricao());
	}

}
