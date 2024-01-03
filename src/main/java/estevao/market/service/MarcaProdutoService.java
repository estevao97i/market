package estevao.market.service;

import estevao.market.exception.MarketException;
import estevao.market.model.CategoriaProduto;
import estevao.market.model.MarcaProduto;
import estevao.market.repository.CategoriaProdutoRepository;
import estevao.market.repository.MarcaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaProdutoService {
    private final MarcaProdutoRepository repository;

    public MarcaProduto salvar(MarcaProduto marcaProduto) {

        if (marcaProduto.getId() == null && repository.existByName(marcaProduto.getNomeDesc().trim().toUpperCase()) > 0) {
            throw new MarketException("Já existe nome dessa marca cadastrada");
        }
        marcaProduto = repository.save(marcaProduto);
        return marcaProduto;
    }

    public void deleteById(Long id) { repository.deleteById(id);}

    public List<MarcaProduto> findByDesc(String desc) {
        return repository.buscarPorDesc(desc.trim().toUpperCase());
    }

    public MarcaProduto findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new MarketException("Marca não encontrada"));
    }
}
