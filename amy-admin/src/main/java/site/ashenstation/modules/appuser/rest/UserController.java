package site.ashenstation.modules.appuser.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.modules.appuser.dto.CreateUserDto;
import site.ashenstation.modules.appuser.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<Integer> createNewUser(CreateUserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }
}
