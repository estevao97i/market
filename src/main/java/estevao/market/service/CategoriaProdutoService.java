package estevao.market.service;

import estevao.market.exception.MarketException;
import estevao.market.model.CategoriaProduto;
import estevao.market.repository.CategoriaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaProdutoService {
    private final CategoriaProdutoRepository repository;
    public CategoriaProduto salvar(CategoriaProduto categoriaProduto) {

        if (categoriaProduto.getId() == null && repository.existByName(categoriaProduto.getNomeDesc().trim().toUpperCase()) > 0) {
            throw new MarketException("Já existe nome de categoria cadastrado");
        }
        categoriaProduto = repository.save(categoriaProduto);
        return categoriaProduto;
    }

    public void deleteById(Long id) { repository.deleteById(id);}
}
