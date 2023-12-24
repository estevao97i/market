package estevao.market.service;

import estevao.market.exception.MarketException;
import estevao.market.model.Produto;
import estevao.market.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Produto salvar(Produto produto) {

        if (produto.getId() == null && repository.existByName(produto.getNome().trim().toUpperCase()) > 0) {
            throw new MarketException("Já existe " + produto.getNome() + " cadastrado");
        }
        produto = repository.save(produto);
        return produto;
    }

    public void deleteById(Long id) { repository.deleteById(id);}

    public List<Produto> findByNome(String desc) {
        return repository.buscarPorNome(desc.trim().toUpperCase());
    }
}
