package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.ContaPagar;
import estevao.market.service.ContaPagarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContaPagarController {

    private final ContaPagarService service;

    @PostMapping(value = "**/salvarContaPagar")
    public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) {

        if (contaPagar == null) {
            throw new MarketException("ContaPagar está vazio");
        }

        if (contaPagar.getEmpresa().getId() == null || contaPagar.getEmpresa() == null) {
            throw new MarketException("Empresa deve ser informada");
        }

        if (contaPagar.getPessoa().getId() == null || contaPagar.getPessoa() == null) {
            throw new MarketException("Pessoa responsável deve ser informada");
        }

        if (contaPagar.getPessoaFornecedor().getId() == null || contaPagar.getPessoaFornecedor() == null) {
            throw new MarketException("Fornecedor responsável deve ser informado");
        }

        if (contaPagar.getDtVencimento() == null) {
            throw new MarketException("A data de vencimento deve ser informada");
        }

        if (contaPagar.getValorTotal() == null) {
            throw new MarketException("O valor total da nota deve ser informado");
        }

        if (contaPagar.getId() == null) {
            var retornoListaDesc = service.buscarPorDescricao(contaPagar.getDescricao().trim().toUpperCase());
            if (retornoListaDesc.size() > 0) throw new MarketException("Descrição duplicada não pode ser aceita");
        }

        return ResponseEntity.ok(service.salvar(contaPagar));
    }

    @GetMapping(value = "**/contaPagarDescricao/{descricao}")
    public ResponseEntity<List<ContaPagar>> findByDescricao(@PathVariable("descricao") String descricao) {

        var listaContaPagarRetornada = service.buscarPorDescricao(descricao);

        return ResponseEntity.ok(listaContaPagarRetornada);
    }

    @GetMapping(value = "**/contaPagarPessoa/{id}")
    public ResponseEntity<List<ContaPagar>> findByPessoa(@PathVariable("id") Long id) {

        var listaContaPagarRetornada = service.buscarPorPessoa(id);

        return ResponseEntity.ok(listaContaPagarRetornada);
    }

    @GetMapping(value = "**/contaPagarFornecedor/{id}")
    public ResponseEntity<List<ContaPagar>> findByFornecedor(@PathVariable("id") Long id) {

        var listaContaPagarRetornada = service.buscarPorFornecedor(id);

        return ResponseEntity.ok(listaContaPagarRetornada);
    }

    @GetMapping(value = "**/contaPagarEmpresa/{id}")
    public ResponseEntity<List<ContaPagar>> findByEmpresa(@PathVariable("id") Long id) {

        var listaContaPagarRetornada = service.buscarPorEmpresa(id);

        return ResponseEntity.ok(listaContaPagarRetornada);
    }

    @GetMapping(value = "**/contaPagar/{id}")
    public ResponseEntity<ContaPagar> findById(@PathVariable("id") Long id) {

        var contaPagarRetornada = service.findById(id);

        return ResponseEntity.ok(contaPagarRetornada);
    }

    @DeleteMapping(value = "**/contaPagar/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        service.deleteById(id);

        return new ResponseEntity("ContaPagar Removida", HttpStatus.OK);
    }
}
