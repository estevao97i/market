package estevao.market.security;

import estevao.market.ApplicationContextLoad;
import estevao.market.model.Usuario;
import estevao.market.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/* Criar autenticacao e retornar autenticacao JWT */
@Service
@Component
public class JWTTokenAutenticacaoService {

    /* Token de validade de 11 dias */
    private final static long EXPIRATION_TIME = 959990000;

    // Chave de senha para juntar acom JWT
    private final static String SECRET = "sdas8a*ad8ar3qwrwefsdfjha///asd";

    private final static String TOKEN_PREFIX = "Bearer";

    private final static String HEADER_STRING = "Authorization";

    // Gera a resposta e retorna o response pro usuario com o JWT
    public void addAuthentication(HttpServletResponse response, String username) throws Exception {

        // Montagem do token

        String JWT = Jwts.builder() // Chama o gerador de Token
                .setSubject(username) // Adiciona o user
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Tempo de Expiração
                .signWith(SignatureAlgorithm.ES512, SECRET).compact(); // tipo de assinatura usando como base o nosso SECRET

        // Ex: Bearer asdasdafrfsdfsdfefga032r.23980hqwerqwberad5asd.239r42yr7qwergiqweADSF
        String token = TOKEN_PREFIX + " " + JWT;

        // retorna a resposta pra tela e para o usuario (API, navegador, aplicativo)
        response.addHeader(HEADER_STRING, token);

        liberacaoCors(response);

        // Usado para o Test API do POSTMAN
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    // Retorna o usuario validado com token ou caso nao seja valido retorna null
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null) {

            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

            // Faz a validacao do token do usuario e extrai o usuario de dentro
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJwt(tokenLimpo)
                    .getBody().getSubject(); // Admin ou Estevao

            if (user != null) {
                Usuario usuario = ApplicationContextLoad
                        .getApplicationContext()
                        .getBean(UsuarioRepository.class)
                        .findUserByLogin(user); // faz a pesquisa no repository e retorna se tem o usuario dentro do banco mesmo

                if (usuario != null) { // se tem o usuario no banco retorna o usuario no response
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getPassword(),
                            usuario.getAuthorities());
                }
            }

        }

        liberacaoCors(response);
        return null;
    }

    // fazendo liberação erro de CORS no navegador
    private void liberacaoCors(HttpServletResponse response) {
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }

        if (response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }

        if (response.getHeader("Access-Control-Request-Headers") == null) {
            response.addHeader("Access-Control-Request-Headers", "*");
        }

        if (response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
