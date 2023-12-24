package estevao.market.controller;

import estevao.market.exception.MarketException;
import estevao.market.model.Acesso;
import estevao.market.model.CategoriaProduto;
import estevao.market.service.CategoriaProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoriaProdutoController {

    private final CategoriaProdutoService service;

    @PostMapping(value = "**/salvarCategoriaProduto")
    public ResponseEntity<CategoriaProduto> salvarCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto) {

        if (categoriaProduto == null) {
            throw new MarketException("Categoria produto está vazia");
        }

        if (categoriaProduto.getEmpresa().getId() == null || categoriaProduto.getEmpresa() == null) {
            throw new MarketException("Empresa deve ser informada");
        }

        return ResponseEntity.ok(service.salvar(categoriaProduto));
    }

    @DeleteMapping(value = "**/deleteCategoria")
    public ResponseEntity<?> delete(@RequestBody CategoriaProduto categoriaProduto) {

        service.deleteById(categoriaProduto.getId());

        return new ResponseEntity("Categoria removida", HttpStatus.OK);
    }
}
