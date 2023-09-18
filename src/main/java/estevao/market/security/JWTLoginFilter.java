package estevao.market.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import estevao.market.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    // Configurando o gerenciamento de autenticacao
    protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        // obriga a autenticar o url
        super(new AntPathRequestMatcher(url));

        // gerenciador de autenticacao
        setAuthenticationManager(authenticationManager);
    }

    // retorna o usuario ao processar autenticacao
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // obtem o usuario do request
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        // retorna user com login e senha
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        try {
            new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        if(failed instanceof BadCredentialsException) {
            response.getWriter().write("User e senha não encontrados");
        } else {
            response.getWriter().write("Falha ao logar" + failed.getMessage());
        }

        // super.unsuccessfulAuthentication(request, response, failed);
    }
}
