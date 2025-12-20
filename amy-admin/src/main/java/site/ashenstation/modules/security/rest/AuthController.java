package site.ashenstation.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.entity.Admin;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.modules.security.dto.AuthenticationDto;
import site.ashenstation.modules.security.dto.JwtUserDto;
import site.ashenstation.modules.security.service.AdminService;
import site.ashenstation.modules.security.vo.AdminInfoVo;
import site.ashenstation.modules.security.vo.AuthResVo;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RsaUtils;
import site.ashenstation.utils.TokenProvider;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "用户认证", description = "用户登入和登出相关接口") // 类级别描述
public class AuthController {

    private final AdminService adminService;

    @AnonymousPostMapping("login")
    @Operation(summary = "用户登录", description = "用户通过用户名和密码登录")
    public ResponseEntity<AuthResVo> login(@RequestBody @Valid AuthenticationDto dto) throws Exception {
        return ResponseEntity.ok(adminService.loginWithUsernamePassword(dto));
    }

    @GetMapping("info")
    @Operation(summary = "获取用户信息", description = "获取用户详细信息")
    public ResponseEntity<AdminInfoVo> getAdminInfo(){
        return ResponseEntity.ok(adminService.getInfo());
    }
}
