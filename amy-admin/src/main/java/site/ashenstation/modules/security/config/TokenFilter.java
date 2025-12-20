package site.ashenstation.modules.security.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.exception.JwtTokenException;
import site.ashenstation.modules.security.service.AdminService;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {

            if (tokenProvider.isTokenExpired(token)) {
                throw new JwtTokenException();
            }

            Claims claims = tokenProvider.getClaims(token);
            String type = claims.get(AmyConstants.JWT_CLAIM_TOKEN_TYPE, String.class);
            String username = claims.get(AmyConstants.JWT_CLAIM_USERNAME, String.class);

            if (JwtTokenType.find(type) == JwtTokenType.LOGIN) {
                List<GrantedAuthority> permissions = new ArrayList<>();
                User principal = new User(claims.getSubject(), "******", permissions);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, permissions));
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(securityProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(securityProperties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(securityProperties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
