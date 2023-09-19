package estevao.market.service;

import estevao.market.exception.MarketException;
import estevao.market.model.Acesso;
import estevao.market.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcessoService {

    @Autowired
    private AcessoRepository repository;

    public Acesso salvar(Acesso acesso) {

        if (acesso.getId() == null) {
            var listaAcessosSalvosPorDesc = repository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());
            if (!listaAcessosSalvosPorDesc.isEmpty()) {
                throw new MarketException("descrição já existente no sistema -> " + acesso.getDescricao());
            }
        }

        return repository.save(acesso);
    }
    public void deleteById(Long id) {
         repository.deleteById(id);
    }

    public List<Acesso> listAll() {
         return repository.findAll();
    }

    public Acesso findById(Long id) {
         return repository.findById(id).orElseThrow(() -> new MarketException("id (" + id + ") não encontrado no sistema."));
    }

    public List<Acesso> buscarPorDesc(String desc) {
         return repository.buscarAcessoDesc(desc.toUpperCase());
    }

    public Acesso update(Acesso acesso) {
         var acessoUpdate = repository.findById(acesso.getId()).get();
         acessoUpdate.setDescricao(acesso.getDescricao());

        return repository.save(acessoUpdate);
    }
}
