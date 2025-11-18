package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.dto.RoleDto;
import site.ashenstation.amyserver.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@Tag(name = "角色管理", description = "所有关于角色管操作的接口")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    @Operation(summary = "添加用户")
    public ResponseEntity<Integer> addRole(RoleDto role) {
        return ResponseEntity.ok(roleService.addRole(role));
    }
}
