package estevao.market;

import estevao.market.controller.AcessoController;
import estevao.market.model.Acesso;
import estevao.market.repository.AcessoRepository;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

		assert acessoSalvo != null;
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

	@Test
	public void testaQueryNativaSql() {
		var acesso = new Acesso();

		acesso.setDescricao("ROLE_ALUNO");
		var acessoSalvo = controller.salvar(acesso).getBody();

		List<Acesso> resultAcesso = repository.buscarAcessoDesc("aluno".toUpperCase().trim());

		assertEquals(1, resultAcesso.size());

		repository.deleteById(acessoSalvo.getId());
	}

}
