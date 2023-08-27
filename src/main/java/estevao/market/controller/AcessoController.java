package estevao.market.controller;

import estevao.market.model.Acesso;
import estevao.market.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/acesso")
public class AcessoController {

    @Autowired
    private AcessoService service;

    @PostMapping()
    public ResponseEntity<Acesso> salvar(Acesso acesso) {
        return ResponseEntity.ok(service.salvar(acesso));
    }
}
