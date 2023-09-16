package estevao.market.repository;

import estevao.market.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query(nativeQuery = true, value = "SELECT u FROM usuario u WHERE u.login = :login")
//    @Query(nativeQuery = true, value = "SELECT u FROM usuario u WHERE u.login = ?1")
    public Usuario findUserByLogin(String login);
}
