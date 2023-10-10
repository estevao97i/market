package estevao.market.repository;

import estevao.market.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    @Query(" Select count(p) from PessoaFisica p where p.cpf = :cpf ")
    Integer existeCpfCadastrado(String cpf);

}
