package site.ashenstation.modules.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.ashenstation.mapper.AdminMapper;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.TokenProvider;

@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;
    private final AdminMapper adminMapper;
    private final CustomTokenMapper customTokenMapper;

    @Override
    public void configure(HttpSecurity builder) {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider, securityProperties, redisUtils, adminMapper, customTokenMapper);
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
