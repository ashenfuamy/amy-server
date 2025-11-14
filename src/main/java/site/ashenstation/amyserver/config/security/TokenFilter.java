package site.ashenstation.amyserver.config.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.amyserver.config.properties.SecurityProperties;
import site.ashenstation.amyserver.dto.OnlineUserDto;
import site.ashenstation.amyserver.service.OnlineUserService;
import site.ashenstation.amyserver.service.UserService;
import site.ashenstation.amyserver.utils.TokenProvider;
import site.ashenstation.amyserver.utils.enums.LoginPlatform;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final OnlineUserService onlineUserService;
    private final UserService userService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String token = resolveToken(httpServletRequest);

        if (StrUtil.isNotBlank(token)) {
            String platform = httpServletRequest.getHeader(securityProperties.getClientHeader());

            String loginKey = tokenProvider.loginKey(token, Objects.requireNonNull(LoginPlatform.find(platform)));
            OnlineUserDto onlineUserDto = onlineUserService.getOne(loginKey);

            if (onlineUserDto != null) {
                tokenProvider.checkRenewal(token);

                Claims claims = tokenProvider.getClaims(token);
                String userId = claims.get("userId", String.class);

                List<GrantedAuthority> permissions = userService.getPermissions(userId);

                User principal = new User(claims.getSubject(), "******", permissions);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, permissions));
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
