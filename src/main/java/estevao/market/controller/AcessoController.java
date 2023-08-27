package estevao.market.controller;

import estevao.market.model.Acesso;
import estevao.market.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AcessoController {

    @Autowired
    private AcessoService service;

    @PostMapping(value = "/salvarAcesso")
    public ResponseEntity<Acesso> salvar(@RequestBody Acesso acesso) {

        var acessoSalvo = service.salvar(acesso);

        return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteAcesso")
    public ResponseEntity<?> delete(@RequestBody Acesso acesso) {

        service.deleteById(acesso.getId());

        return new ResponseEntity("Acesso removido", HttpStatus.OK);
    }
}
