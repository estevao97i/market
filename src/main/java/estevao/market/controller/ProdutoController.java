package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.Produto;
import estevao.market.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping(value = "**/salvarCategoriaProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody Produto produto) {

        if (produto == null) {
            throw new MarketException("Produto está vazio");
        }

        if (produto.getEmpresa().getId() == null || produto.getEmpresa() == null) {
            throw new MarketException("Empresa deve ser informada");
        }

        return ResponseEntity.ok(service.salvar(produto));
    }

    @DeleteMapping(value = "**/Produto")
    public ResponseEntity<?> delete(@RequestBody Produto produto) {

        service.deleteById(produto.getId());

        return new ResponseEntity("Produto Removido", HttpStatus.OK);
    }

    @GetMapping(value = "**/Produto/{nome}")
    public ResponseEntity<List<Produto>> findByDesc(@PathVariable("nome") String nome) {

        return ResponseEntity.ok(service.findByNome(nome));
    }
}
