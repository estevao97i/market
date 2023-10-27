package estevao.market.service;

import estevao.market.model.CategoriaProduto;
import estevao.market.repository.CategoriaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaProdutoService {
    private final CategoriaProdutoRepository repository;
    public CategoriaProduto salvar(CategoriaProduto categoriaProduto) {
        categoriaProduto = repository.save(categoriaProduto);
        return categoriaProduto;
    }
}
