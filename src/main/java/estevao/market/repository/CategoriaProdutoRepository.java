package estevao.market.repository;

import estevao.market.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query("select count(cp) from CategoriaProduto cp where trim(upper(cp.nomeDesc)) = :nomeDesc")
    Integer existByName(String nomeDesc);
}
