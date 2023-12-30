package estevao.market.repository;

import estevao.market.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("select count(p) from Produto p where trim(upper(p.nome)) = :nome")
    Integer existByName(String nome);

    @Query("select p from Produto p where upper(trim(p.nome)) like '%:nome%'")
    List<Produto> buscarPorNome(String nome);
}
