package estevao.market.repository;

import estevao.market.model.PessoaJuridica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

    @Query("select count(p) from Pessoa p where p.cnpj = :cnpj")
    public Integer existeCnpjCadastrado(String cnpj);
}
