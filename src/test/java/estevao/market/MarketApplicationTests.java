package estevao.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import estevao.market.controller.AcessoController;
import estevao.market.model.Acesso;
import estevao.market.repository.AcessoRepository;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest(classes = MarketApplication.class)
class MarketApplicationTests extends TestCase {

	@Autowired
	private AcessoController controller;

	@Autowired
	private AcessoRepository repository;

	@Autowired
	private WebApplicationContext wac;

	@Test
	public void testRestApiCadastrarAcesso() throws Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_BUILDER");

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
						.content(obj.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		Acesso objAcessoRetorno = obj.readValue(
				retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);

		assertEquals(acesso.getDescricao(), objAcessoRetorno.getDescricao());
	}

	@Test
	public void testRestApiDeletarAcesso() throws Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE");
		repository.save(acesso);

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso")
						.content(obj.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		Acesso objAcessoRetorno = obj.readValue(
				retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);

		assertEquals(acesso.getDescricao(), objAcessoRetorno.getDescricao());
	}

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
