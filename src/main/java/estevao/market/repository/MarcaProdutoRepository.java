package estevao.market.repository;

import estevao.market.model.MarcaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

    @Query("select count(mp) from MarcaProduto mp where trim(upper(mp.nomeDesc)) = :nomeDesc")
    Integer existByName(String nomeDesc);

    @Query("select mc from MarcaProduto mc where upper(trim(mc.nomeDesc)) like '%:desc%'")
    List<MarcaProduto> buscarPorDesc(String desc);
}
