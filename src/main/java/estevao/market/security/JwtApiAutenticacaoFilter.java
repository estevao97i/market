package estevao.market.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filtro que captura todas as requisicoes para autenticacao
public class JwtApiAutenticacaoFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        // Estabelece a autenticacao do usuario

        Authentication authentication = new JWTTokenAutenticacaoService().getAuthentication(
                (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

        // coloca o processo de autenticacao para o spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(servletRequest, servletResponse);

    }
}
