package site.ashenstation.modules.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.TokenProvider;

@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity builder) {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider, securityProperties);
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
