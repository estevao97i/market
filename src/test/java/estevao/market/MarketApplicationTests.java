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
		var acessoSalvo = repository.save(acesso);

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcesso")
						.content(obj.writeValueAsString(acessoSalvo))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("RETORNO API " + retornoApi.andReturn().getResponse().getContentAsString()); // retorna o conteudo da requisição
		System.out.println("STATUS " + retornoApi.andReturn().getResponse().getStatus()); // retorna o status da requisição

		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	}

	@Test
	public void testRestApiDeletarAcessoPorId() throws Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		Acesso acessoSalvo = repository.save(acesso);

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcesso/" + acessoSalvo.getId())
						.content(obj.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("RETORNO API " + retornoApi.andReturn().getResponse().getContentAsString()); // retorna o conteudo da requisição
		System.out.println("STATUS " + retornoApi.andReturn().getResponse().getStatus()); // retorna o status da requisição

		assertEquals("Acesso removido pelo id", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	}

	@Test
	public void testRestApiBuscarAcessoPorId() throws Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_CREATE_ID");
		Acesso acessoSalvo = repository.save(acesso);

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/" + acessoSalvo.getId())
						.content(obj.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("RETORNO API " + retornoApi.andReturn().getResponse().getContentAsString()); // retorna o conteudo da requisição
		System.out.println("STATUS " + retornoApi.andReturn().getResponse().getStatus()); // retorna o status da requisição

//		assertEquals(obj.writeValueAsString(acessoSalvo), retornoApi.andReturn().getResponse().getContentAsString());
		var objAcesso = obj.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		assertEquals(objAcesso, acessoSalvo);
	}

	// TODO
	@Test
	public void testRestApiBuscarTodosAcessos() throws Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_CREATE_ID");
		repository.save(acesso);

		var listaAcessos = repository.findAll();

		ObjectMapper obj = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

//		System.out.println("RETORNO API " + retornoApi.andReturn().getResponse().); // retorna o conteudo da requisição
//		System.out.println("STATUS " + retornoApi.andReturn().getResponse().getStatus()); // retorna o status da requisição

//		assertEquals(obj.writeValueAsString(acessoSalvo), retornoApi.andReturn().getResponse().getContentAsString());
//		var objAcesso = obj.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
//		assertEquals(listaAcessos.size(), retornoApi.andReturn().getResponse());
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
