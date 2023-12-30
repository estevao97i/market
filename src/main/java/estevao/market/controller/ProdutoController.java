package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.Produto;
import estevao.market.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping(value = "**/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) {

        if (produto == null) {
            throw new MarketException("Produto está vazio");
        }

        if (produto.getEmpresa().getId() == null || produto.getEmpresa() == null) {
            throw new MarketException("Empresa deve ser informada");
        }

        if (produto.getCategoriaProduto().getId() == null || produto.getCategoriaProduto() == null) {
            throw new MarketException("Categoria do Produto deve ser informada");
        }

        if (produto.getMarcaProduto().getId() == null || produto.getMarcaProduto() == null) {
            throw new MarketException("Marca do Produto deve ser informada");
        }

        return ResponseEntity.ok(service.salvar(produto));
    }

    @DeleteMapping(value = "**/produto")
    public ResponseEntity<?> delete(@RequestBody Produto produto) {

        service.deleteById(produto.getId());

        return new ResponseEntity("Produto Removido", HttpStatus.OK);
    }

    @GetMapping(value = "**/produto/{nome}")
    public ResponseEntity<List<Produto>> findByDesc(@PathVariable("nome") String nome) {

        return ResponseEntity.ok(service.findByNome(nome));
    }
}
