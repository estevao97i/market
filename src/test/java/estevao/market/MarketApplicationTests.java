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

	@Autowired
	private AcessoRepository repository;

	public Acesso criarObjetoNoBanco() {
		var acessoSalvo = new Acesso();
		acessoSalvo.setDescricao("ROLE_TESTE");
		repository.saveAndFlush(acessoSalvo);
		return acessoSalvo;
	}

	@Test
	public void testaCadastrarAcessoController() {

		// teste de salvar na camada de Resource
		var acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");

		var acessoSalvo = controller.salvar(acesso).getBody();

		assertTrue(acessoSalvo.getId() > 0);
		assertEquals("ROLE_ADMIN", acessoSalvo.getDescricao());

	}

	@Test
	public void testaDeletarAcessoRepository() {
		var acesso = criarObjetoNoBanco();

		repository.deleteById(acesso.getId());
		repository.flush();

		var acessoRetornado = repository.findById(acesso.getId()).orElse(null);

		assertNull(acessoRetornado);
	}

}
