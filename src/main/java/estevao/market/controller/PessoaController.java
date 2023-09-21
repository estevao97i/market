package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.PessoaJuridica;
import estevao.market.repository.PessoaRepository;
import estevao.market.service.PessoaUserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {

    @Autowired
    PessoaRepository repository;

    @Autowired
    PessoaUserService service;

    @PostMapping(value = "**/SalvarPessoaJuridica")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws MarketException {

        if (pessoaJuridica == null) {
            throw new MarketException("Não foi possível salvar. Pessoa Jurídica -> NULL");
        }

        if (pessoaJuridica.getId() == null && repository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) > 0) {
            throw new MarketException("CNPJ já cadastrado -> " + pessoaJuridica.getCnpj());
        }

        service.salvarPj(pessoaJuridica);

        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }
}
