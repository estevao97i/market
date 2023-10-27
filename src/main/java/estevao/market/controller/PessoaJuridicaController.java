package estevao.market.controller;

import estevao.market.dto.CepDTO;
import estevao.market.dto.CnpjDTO;
import estevao.market.exception.MarketException;
import estevao.market.model.PessoaJuridica;
import estevao.market.repository.EnderecoRepository;
import estevao.market.repository.PessoaJuridicaRepository;
import estevao.market.service.PessoaJuridicaUserService;
import estevao.market.utils.ValidateCnpj;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PessoaJuridicaController {

    private final PessoaJuridicaRepository repository;
    private final PessoaJuridicaUserService service;
    private final EnderecoRepository enderecoRepository;

    @PostMapping(value = "**/SalvarPessoaJuridica")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws MarketException {

        if (pessoaJuridica == null) {
            throw new MarketException("Não foi possível salvar. Pessoa Jurídica -> NULL");
        }

        if (pessoaJuridica.getTipoPessoa() == null) {
            throw new MarketException("Informe o tipo -> Jurídico ou Fornecedor da Loja");
        }

        if (pessoaJuridica.getId() == null && repository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) > 0) {
            throw new MarketException("CNPJ já cadastrado -> " + pessoaJuridica.getCnpj());
        }

        if (pessoaJuridica.getId() == null && repository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) > 0) {
            throw new MarketException("Inscrição Estadual já cadastrada -> " + pessoaJuridica.getInscEstadual());
        }

        if (!ValidateCnpj.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new MarketException("CNPJ inválido -> " + pessoaJuridica.getCnpj());
        }

        // insere puxando da api de cep pelo cep passado na request
        if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                var enderecoPorCep = this.buscaPorCep(pessoaJuridica.getEnderecos().get(p).getCep()).getBody(); // pesquisar na api de CEP e injetar no mesmo objeto
                assert enderecoPorCep != null;
                pessoaJuridica.getEnderecos().get(p).setBairro(enderecoPorCep.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(enderecoPorCep.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setUf(enderecoPorCep.getUf());
                pessoaJuridica.getEnderecos().get(p).setComplemento(enderecoPorCep.getComplemento());
                pessoaJuridica.getEnderecos().get(p).setRuaLogra(enderecoPorCep.getLogradouro());
            }
        } else {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                var enderecoRetorno = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
                if (!enderecoRetorno.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
                    var enderecoPorCep = this.buscaPorCep(pessoaJuridica.getEnderecos().get(p).getCep()).getBody(); // pesquisar na api de CEP e injetar no mesmo objeto
                    assert enderecoPorCep != null;
                    pessoaJuridica.getEnderecos().get(p).setBairro(enderecoPorCep.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(enderecoPorCep.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setUf(enderecoPorCep.getUf());
                    pessoaJuridica.getEnderecos().get(p).setComplemento(enderecoPorCep.getComplemento());
                    pessoaJuridica.getEnderecos().get(p).setRuaLogra(enderecoPorCep.getLogradouro());
                }
            }
        }

        service.salvarPj(pessoaJuridica);

        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }

    @GetMapping(value = "**/buscarPorNomePj/{nome}")
    public ResponseEntity<List<PessoaJuridica>> obterPorNome(@PathVariable("nome") String nome) {
        service.contaRequisicoesEndPoint("buscarPorNomePj");
        var pessoaJuridicaPorNome = repository.pesquisaPorNomePJ(nome.trim().toUpperCase());
        return new ResponseEntity<>(pessoaJuridicaPorNome, HttpStatus.OK);
    }
    @GetMapping(value = "**/buscarPorCnpjPJ/{cnpj}")
    public ResponseEntity<List<PessoaJuridica>> obterPorCpf(@PathVariable("cnpj") String cnpj) {
        service.contaRequisicoesEndPoint("buscarPorCnpjPJ");
        var pessoaFisicaPorCnpj = repository.pesquisaPorCNPJ(cnpj);
        return new ResponseEntity<>(pessoaFisicaPorCnpj, HttpStatus.OK);
    }

    @GetMapping(value = "consultaCep/{cep}")
    public ResponseEntity<CepDTO> buscaPorCep(@PathVariable("cep") String cep) {
        var cepBuscado = service.consultaCep(cep);
        return ResponseEntity.ok(cepBuscado);
    }

    @GetMapping(value = "consultaCnpj/{cnpj}")
    public ResponseEntity<CnpjDTO> consultaCnpj(@PathVariable("cnpj") String cnpj) {
        var cnpjRetornado = service.consultaCNPJ(cnpj);
        return ResponseEntity.ok(cnpjRetornado);
    }

}
