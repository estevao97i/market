package estevao.market.controller;

import estevao.market.model.Acesso;
import estevao.market.service.AcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AcessoController {
    private final AcessoService service;
    @GetMapping(value = "**/listarTodos")
    public ResponseEntity<List<Acesso>> listAll() {

        List<Acesso> acessos = service.listAll();

        return new ResponseEntity<>(acessos, HttpStatus.OK);
    }

    @GetMapping(value = "**/encontrarId/{id}")
    public ResponseEntity<Acesso> findById(@PathVariable("id") Long id) {

        Acesso acesso = service.findById(id);

        return new ResponseEntity<>(acesso, HttpStatus.OK);
    }

    @GetMapping(value = "**/buscarPorDesc/{desc}" )
    public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<Acesso> listAcesso = service.buscarPorDesc(desc);

        return new ResponseEntity<>(listAcesso, HttpStatus.OK);
    }

    @PostMapping(value = "**/salvarAcesso")
    public ResponseEntity<Acesso> salvar(@RequestBody Acesso acesso) {

        var acessoSalvo = service.salvar(acesso);

        return new ResponseEntity<>(acessoSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "**/deleteAcesso")
    public ResponseEntity<?> delete(@RequestBody Acesso acesso) {

        service.deleteById(acesso.getId());

        return new ResponseEntity("Acesso removido", HttpStatus.OK);
    }

    @DeleteMapping(value = "**/deleteAcesso/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        service.deleteById(id);

        return new ResponseEntity("Acesso removido pelo id", HttpStatus.OK);
    }

    @PutMapping(value = "**/updateAcesso")
    public ResponseEntity<Acesso> update(@RequestBody Acesso acesso) {

        var acessoUpdate = service.update(acesso);

        return new ResponseEntity<>(acessoUpdate, HttpStatus.OK);
    }
}
