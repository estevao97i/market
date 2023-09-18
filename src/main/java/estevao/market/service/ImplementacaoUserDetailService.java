package estevao.market.service;

import estevao.market.model.Usuario;
import estevao.market.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImplementacaoUserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findUserByLogin(s); /* recebe o parametro de login para consulta (String s) */

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
    }
}
