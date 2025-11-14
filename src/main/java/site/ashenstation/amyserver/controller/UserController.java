package site.ashenstation.amyserver.controller;

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
public class UserController {

    private final UserService userService;

    @AnonymousPostMapping("register")
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterUerDto uerDto) {
        return ResponseEntity.ok(userService.register(uerDto));
    }
}
