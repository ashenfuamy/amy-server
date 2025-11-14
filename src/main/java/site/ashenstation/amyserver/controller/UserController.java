package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.dto.RegisterUerDto;
import site.ashenstation.amyserver.service.UserService;
import site.ashenstation.amyserver.utils.annotation.rest.AnonymousPostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "所有关于用户操作的接口")
public class UserController {

    private final UserService userService;

    @AnonymousPostMapping("register")
    @Operation(summary = "用户注册", description = "用户注册")
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterUerDto uerDto) {
        return ResponseEntity.ok(userService.register(uerDto));
    }
}
