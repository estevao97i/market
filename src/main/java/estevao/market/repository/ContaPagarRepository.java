package estevao.market.repository;

import estevao.market.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Query("select cp from ContaPagar cp where trim(upper(cp.descricao)) = :descricao")
    List<ContaPagar> buscarPorDescricao(String descricao);

    @Query("select cp from ContaPagar cp where cp.pessoa.id = :pessoaId")
    List<ContaPagar> buscarPorPessoa(Long pessoaId);

    @Query("select cp from ContaPagar cp where cp.pessoaFornecedor.id = :fornercedorId")
    List<ContaPagar> buscarPorFornecedor(Long fornercedorId);

    @Query("select cp from ContaPagar cp where cp.empresa.id = :empresaId")
    List<ContaPagar> buscarPorEmpresa(Long empresaId);
}
