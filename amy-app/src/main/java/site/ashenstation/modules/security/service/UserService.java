package site.ashenstation.modules.security.service;

import com.mybatisflex.core.update.UpdateChain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.entity.User;
import site.ashenstation.enums.LoginPlatform;
import site.ashenstation.mapper.UserMapper;
import site.ashenstation.modules.security.dto.AuthenticationDto;
import site.ashenstation.modules.security.dto.JwtUserDto;
import site.ashenstation.modules.security.vo.AuthVo;
import site.ashenstation.properties.LoginProperties;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RsaUtils;
import site.ashenstation.utils.TokenProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final LoginProperties loginProperties;
    private final SecurityProperties securityProperties;
    private final UserMapper userMapper;


    @Transactional
    public AuthVo loginWithUsernamePassword(AuthenticationDto dto, HttpServletRequest request) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, dto.getPassword());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        JwtUserDto jwtUserDto = (JwtUserDto) authenticate.getPrincipal();
        User user = jwtUserDto.getUser();

        HashMap<String, String> claims = new HashMap<>();
        claims.put(AmyConstants.JWT_CLAIM_USER_ID, String.valueOf(user.getId()));
        claims.put(AmyConstants.JWT_CLAIM_USERNAME, user.getUsername());

        String token = tokenProvider.createToken(user.getUsername(), claims);

        if (loginProperties.isSingleLogin()) {
            onlineUserService.kickOutForUsernameAndPlatform(user.getUsername(), Objects.requireNonNull(LoginPlatform.find(request.getHeader(securityProperties.getClientHeader()))));
        }

        onlineUserService.save(user.getUsername(), token, request);

        UpdateChain
                .of(User.class)
                .set(User::getLastLogin, new Date())
                .where(User::getId).eq(user.getId())
                .update();

        return new AuthVo(token, user);
    }
}
