package site.ashenstation.modules.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.ashenstation.mapper.UserResourcePermissionMapper;
import site.ashenstation.modules.security.service.OnlineUserService;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.TokenProvider;

@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;
    private final UserResourcePermissionMapper userResourcePermissionMapper;

    @Override
    public void configure(HttpSecurity builder) {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider, securityProperties, onlineUserService, redisUtils, userResourcePermissionMapper);
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
