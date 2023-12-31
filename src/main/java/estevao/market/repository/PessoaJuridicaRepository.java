package estevao.market.repository;

import estevao.market.model.PessoaFisica;
import estevao.market.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

    @Query(" select count(p) from PessoaJuridica p where p.cnpj = :cnpj ")
    Integer existeCnpjCadastrado(String cnpj);

    @Query(" select p from PessoaJuridica p where p.cnpj = :cnpj ")
    PessoaJuridica existeCnpjCadastradoReturnObj(String cnpj);

    @Query(" select count(p) from PessoaJuridica p where p.inscEstadual = :inscEstadual ")
    Integer existeInsEstadualCadastrado(String inscEstadual);

    @Query(" Select p from PessoaJuridica p where trim(upper(p.nome)) like %:nome% ")
    List<PessoaJuridica> pesquisaPorNomePJ(String nome);

    @Query(" Select p from PessoaJuridica p where p.cnpj = :cnpj ")
    List<PessoaJuridica> pesquisaPorCNPJ(String cnpj);
}
