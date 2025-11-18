package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.amyserver.dto.RoleDto;
import site.ashenstation.amyserver.service.RoleService;
import site.ashenstation.amyserver.vo.RoleVo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@Tag(name = "角色管理", description = "所有关于角色管操作的接口")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    @Operation(summary = "添加角色")
    public ResponseEntity<Boolean> addRole(@RequestBody @Validated RoleDto role) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有角色")
    public ResponseEntity<List<RoleVo>> getAllRole() {
        return ResponseEntity.ok(roleService.getAllRole());
    }

}
