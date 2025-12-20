package site.ashenstation.modules.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }
}
