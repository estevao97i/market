package estevao.market;

import estevao.market.model.Acesso;
import estevao.market.repository.AcessoRepository;
import estevao.market.service.AcessoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MarketApplication.class)
class MarketApplicationTests {

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@Test
	public void tentaCadastrarAcesso() {

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");

		acessoRepository.save(acesso);
	}

}
