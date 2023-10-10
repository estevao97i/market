package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.PessoaJuridica;
import estevao.market.repository.PessoaJuridicaRepository;
import estevao.market.service.PessoaJuridicaUserService;
import estevao.market.utils.ValidateCnpj;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PessoaJuridicaController {

    private final PessoaJuridicaRepository repository;

    private final PessoaJuridicaUserService service;

    @PostMapping(value = "**/SalvarPessoaJuridica")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws MarketException {

        if (pessoaJuridica == null) {
            throw new MarketException("Não foi possível salvar. Pessoa Jurídica -> NULL");
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

        service.salvarPj(pessoaJuridica);

        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }
}
