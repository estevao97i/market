package estevao.market.controller;

import estevao.market.enums.TipoPessoa;
import estevao.market.exception.MarketException;
import estevao.market.model.PessoaFisica;
import estevao.market.repository.PessoaFisicaRepository;
import estevao.market.service.PessoaFisicaUserService;
import estevao.market.utils.ValidateCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PessoaFisicaController {

    private final PessoaFisicaUserService pessoaService;

    private final PessoaFisicaRepository pessoaRepository;

    @PostMapping("**/salvarPf")
    public ResponseEntity<PessoaFisica> inserePf(@RequestBody @Valid PessoaFisica pessoaFisica) {

        if (!ValidateCpf.isCPF(pessoaFisica.getCpf())) {
            throw new MarketException("CPF inválido -> " + pessoaFisica.getCpf());
        }

        if (pessoaFisica.getTipoPessoa() == null) {
            pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
        }

        if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) > 0) {
            throw new MarketException("CPF já cadastrado -> " + pessoaFisica.getCpf());
        }

        pessoaService.salvarPf(pessoaFisica);

        return new ResponseEntity<>(pessoaFisica, HttpStatus.OK);
    }

    @GetMapping(value = "**/buscarPorNomePf/{nome}")
    public ResponseEntity<List<PessoaFisica>> obterPorNome(@PathVariable("nome") String nome) {
        var pessoaFisicaPorNome = pessoaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
        return new ResponseEntity<>(pessoaFisicaPorNome, HttpStatus.OK);
    }

    @GetMapping(value = "**/buscarPorCpfPf/{cpf}")
    public ResponseEntity<List<PessoaFisica>> obterPorCpf(@PathVariable("cpf") String cpf) {
        var pessoaFisicaPorCpf = pessoaRepository.pesquisaPorCPF(cpf);
        return new ResponseEntity<>(pessoaFisicaPorCpf, HttpStatus.OK);
    }
}
