package estevao.market.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

        // Usado para o Test API do POSTMAN
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }
}
