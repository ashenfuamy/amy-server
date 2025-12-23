package site.ashenstation.modules.security.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.entity.UserResourcePermission;
import site.ashenstation.entity.table.UserResourcePermissionTableDef;
import site.ashenstation.enums.LoginPlatform;
import site.ashenstation.mapper.UserResourcePermissionMapper;
import site.ashenstation.modules.security.dto.OnlineUserDto;
import site.ashenstation.modules.security.service.OnlineUserService;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;
    private final UserResourcePermissionMapper userResourcePermissionMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String token = resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {
            String platform = httpServletRequest.getHeader(securityProperties.getClientHeader());

            String loginKey = tokenProvider.loginKey(token, Objects.requireNonNull(LoginPlatform.find(platform)));
            OnlineUserDto onlineUserDto = onlineUserService.getOne(loginKey);

            if (onlineUserDto != null) {
                tokenProvider.checkRenewal(token);
                Claims claims = tokenProvider.getClaims(token);
                List<GrantedAuthority> permissions = new ArrayList<>();

                String username = claims.getSubject();
                String userId = claims.get(AmyConstants.JWT_CLAIM_USER_ID, String.class);

                String key = securityProperties.getResourcePermissionKey() + username;
                Set<Object> members = redisUtils.members(key);

                if (members != null && !members.isEmpty()) {
                    for (Object obj : members) {
                        if (obj instanceof String) {
                            permissions.add(new SimpleGrantedAuthority((String) obj));
                        }
                    }
                } else {
                    List<UserResourcePermission> userResourcePermissions = userResourcePermissionMapper.selectListByCondition(
                            UserResourcePermissionTableDef.USER_RESOURCE_PERMISSION.USER_ID.eq(userId)
                    );

                    userResourcePermissions.forEach(up -> {
                       if (up.getExpireTimestamp() == null || new Date().before(up.getExpireTimestamp())) {
                           String permission = String.valueOf(up.getResourceId());
                           permissions.add(new SimpleGrantedAuthority(permission));
                           redisUtils.sSet(key, permission);
                       }
                    });
                }

                User principal = new User(claims.getSubject(), "******", permissions);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, permissions));
            }
        }

        filterChain.doFilter(servletRequest,servletResponse);
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
