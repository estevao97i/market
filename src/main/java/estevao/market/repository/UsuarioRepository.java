package estevao.market.repository;

import estevao.market.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.login = ?1")
    Usuario findUserByLogin(String login);

    @Query("select u from Usuario u where u.pessoa.id = :id " +
            " or u.pessoa.email = :email")
    Usuario findByPessoa(Long id, String email);

    @Query(value = " select constraint_name from information_schema.constraint_column_usage " +
            " where table_name = 'usuario_acesso' " +
            " and column_name = 'acesso_id' " +
            " and constraint_name <> 'unique_acesso_user'; ", nativeQuery = true)
    String consultaConstraintRole();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " insert into usuario_acesso(usuario_id, acesso_id) values (:id, " +
            "(select id from acesso where descricao = 'ROLE_USER' ))")
    void insereAcessoUserPj(Long id);
}
