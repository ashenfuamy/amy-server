package site.ashenstation.modules.security.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.modules.security.dto.AuthenticationDto;
import site.ashenstation.modules.security.service.UserService;
import site.ashenstation.modules.security.vo.AuthVo;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "用户管理", description = "所有关于用户操作的接口")
public class AuthController {
    private final UserService userService;

    @AnonymousPostMapping("login")
    @Operation(summary = "用户登录", description = "用户名密码登录")
    public ResponseEntity<AuthVo> login(@RequestBody @Valid AuthenticationDto dto, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(userService.loginWithUsernamePassword(dto, request));
    }
}
