package estevao.market.repository;

import estevao.market.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

//    @Query(" SELECT u FROM usuario u WHERE u.login = :login ")
    @Query("SELECT u FROM Usuario u WHERE u.login = ?1")
    public Usuario findUserByLogin(String login);
}
