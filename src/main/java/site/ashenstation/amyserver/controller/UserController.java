package site.ashenstation.amyserver.controller;

import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.amyserver.config.properties.LoginProperties;
import site.ashenstation.amyserver.config.properties.RsaProperties;
import site.ashenstation.amyserver.config.properties.SecurityProperties;
import site.ashenstation.amyserver.dto.*;
import site.ashenstation.amyserver.entity.UserPo;
import site.ashenstation.amyserver.service.OnlineUserService;
import site.ashenstation.amyserver.service.UserService;
import site.ashenstation.amyserver.utils.RsaUtils;
import site.ashenstation.amyserver.utils.SecurityUtils;
import site.ashenstation.amyserver.utils.TokenProvider;
import site.ashenstation.amyserver.utils.annotation.rest.AnonymousPostMapping;
import site.ashenstation.amyserver.utils.enums.LoginPlatform;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "所有关于用户操作的接口")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final LoginProperties loginProperties;
    private final OnlineUserService onlineUserService;
    private final SecurityProperties securityProperties;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册")
    @PreAuthorize("hasAuthority('user:register')")
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterUerDto uerDto) {
        return ResponseEntity.ok(userService.register(uerDto));
    }

    @AnonymousPostMapping("login")
    @Operation(summary = "用户登录", description = "用户名密码登录")
    public ResponseEntity<HashMap<Object, Object>> login(@RequestBody @Valid AuthenticationDto dto, HttpServletRequest request) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, dto.getPassword());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        JwtUserDto jwtUserDto = (JwtUserDto) authenticate.getPrincipal();
        UserPo user = jwtUserDto.getUser();

        user.setPassword(null);

        HashMap<String, String> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put(TokenProvider.AUTHORITIES_UUID_KEY, IdUtil.fastSimpleUUID());

        String token = tokenProvider.createToken(user.getUsername(), claims);

        if (loginProperties.isSingleLogin()) {
            onlineUserService.kickOutForUsernameAndPlatform(user.getUsername(), Objects.requireNonNull(LoginPlatform.find(request.getHeader(securityProperties.getClientHeader()))));
        }

        onlineUserService.save(user.getUsername(), token, request);

        userService.recordLoginDataTime(user.getId());

        return ResponseEntity.ok(new HashMap<>() {{
            put("token", token);
            put("user", user);
        }});
    }

    @PostMapping(value = "set-role", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "设置用户角色")
    @PreAuthorize("hasAuthority('user:set-role')")
    public ResponseEntity<Boolean> setRole(@RequestBody @Validated SetUserRolesDto dto) {
        return ResponseEntity.ok(userService.setUserRoles(dto));
    }


    @DeleteMapping(value = "logout")
    @Operation(summary = "退出登录")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        onlineUserService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "info")
    @Operation(summary = "获取用户信息")
    public ResponseEntity<UserDetails> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    }


    @PutMapping("update")
    @Operation(summary = "更新用户信息")
    public ResponseEntity<?> updateUserInfo(UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.setUserInfo(updateUserDto));
    }

}
