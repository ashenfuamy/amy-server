package site.ashenstation.modules.security.config;

import com.mybatisflex.core.query.QueryWrapper;
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
import site.ashenstation.entity.Admin;
import site.ashenstation.entity.CustomToken;
import site.ashenstation.entity.Permission;
import site.ashenstation.entity.table.AdminTableDef;
import site.ashenstation.entity.table.CustomTokenTableDef;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.exception.JwtTokenException;
import site.ashenstation.mapper.AdminMapper;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.mapper.PermissionMapper;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;
    private final AdminMapper adminMapper;
    private final CustomTokenMapper customTokenMapper;
    private final PermissionMapper permissionMapper;


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

            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            if (JwtTokenType.find(type) == JwtTokenType.LOGIN) {

                String key = securityProperties.getResourcePermissionKey() + username;
                Set<Object> members = redisUtils.members(key);

                if (members != null && !members.isEmpty()) {
                    for (Object obj : members) {
                        if (obj instanceof String) {
                            grantedAuthorities.add(new SimpleGrantedAuthority((String) obj));
                        }
                    }
                } else {

                    Admin admin
                            = adminMapper.selectOneWithRelationsByQuery(QueryWrapper.create().select().where(AdminTableDef.ADMIN.USERNAME.eq(username)));

                    Boolean isSuperAdmin = admin.getIsSuperAdmin();

                    if (isSuperAdmin) {
                        List<Permission> permissions = permissionMapper.selectAll();
                        for (Permission permission : permissions) {
                            redisUtils.sSet(key, permission.getField() + ":" + permission.getCode());
                            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getField() + ":" + permission.getCode()));
                        }
                    } else {
                        List<Permission> permissions = admin.getPermissions();
                        permissions.forEach(c -> {
                            String p = c.getField() + ":" + c.getCode();
                            redisUtils.sSet(key, p);
                            grantedAuthorities.add(new SimpleGrantedAuthority(p));
                        });
                    }
                }
            }

            if (JwtTokenType.find(type) == JwtTokenType.Authorization) {
                String uid = claims.get(AmyConstants.JWT_CLAIM_UID, String.class);

                CustomToken customToken = customTokenMapper.selectOneByQuery(QueryWrapper.create().select().where(CustomTokenTableDef.CUSTOM_TOKEN.UID.eq(uid)));

                if (customToken == null) {
                    throw new BadRequestException("无效 Token");
                }

                String permission = claims.get(AmyConstants.JWT_CLAIM_PERMISSION, String.class);

                String[] split = permission.split(",");
                List<String> permissions = Arrays.asList(split);

                permissions.forEach(p -> {
                    grantedAuthorities.add(new SimpleGrantedAuthority(p));
                });
            }

            User principal = new User(claims.getSubject(), "******", grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, grantedAuthorities));

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
