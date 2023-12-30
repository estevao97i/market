package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.MarcaProduto;
import estevao.market.service.MarcaProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MarcaProdutoController {

    private final MarcaProdutoService service;

    @PostMapping(value = "**/salvarMarcaProduto")
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody MarcaProduto marcaProduto) {

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

    @GetMapping(value = "**/marcaProduto/{desc}")
    public ResponseEntity<List<MarcaProduto>> findByDesc(@PathVariable("desc") String desc) {

        return ResponseEntity.ok(service.findByDesc(desc));
    }
}
