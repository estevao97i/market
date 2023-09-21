package estevao.market.service;

import estevao.market.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository repository;


}
