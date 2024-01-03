package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.MarcaProduto;
import estevao.market.service.MarcaProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MarcaProdutoController {

    private final MarcaProdutoService service;

    @PostMapping(value = "**/salvarMarcaProduto")
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto) {

        if (marcaProduto == null) {
            throw new MarketException("Marca produto está vazia");
        }

        if (marcaProduto.getEmpresa().getId() == null || marcaProduto.getEmpresa() == null) {
            throw new MarketException("Empresa deve ser informada");
        }

        return ResponseEntity.ok(service.salvar(marcaProduto));
    }

    @DeleteMapping(value = "**/marcaProduto")
    public ResponseEntity<?> delete(@RequestBody MarcaProduto marcaProduto) {

        service.deleteById(marcaProduto.getId());

        return new ResponseEntity("Marca removida", HttpStatus.OK);
    }

    @GetMapping(value = "**/marcaProduto")
    public ResponseEntity<List<MarcaProduto>> findByDesc(@RequestBody MarcaProduto marcaProduto) {

        return ResponseEntity.ok(service.findByDesc(marcaProduto.getNomeDesc()));
    }

    @GetMapping(value = "**/marcaProduto/{id}")
    public ResponseEntity<MarcaProduto> findById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.findById(id));
    }
}
