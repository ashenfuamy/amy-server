package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.amyserver.dto.CreatePermissionDto;
import site.ashenstation.amyserver.dto.RoleDto;
import site.ashenstation.amyserver.dto.SetRolePermissionDto;
import site.ashenstation.amyserver.service.RoleService;
import site.ashenstation.amyserver.vo.PermissionVo;
import site.ashenstation.amyserver.vo.RoleVo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@Tag(name = "角色管理", description = "所有关于角色管操作的接口")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("add")
    @Operation(summary = "添加角色")
    @PreAuthorize("hasAuthority('role:add')")
    public ResponseEntity<Boolean> addRole(@RequestBody @Validated RoleDto role) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

    @GetMapping("list")
    @Operation(summary = "获取所有角色")
    @PreAuthorize("hasAuthority('role:getall')")
    public ResponseEntity<List<RoleVo>> getAllRole() {
        return ResponseEntity.ok(roleService.getAllRole());
    }

    @PostMapping("add-permission")
    @Operation(summary = "添加权限")
    @PreAuthorize("hasAuthority('role:create-permission')")
    public ResponseEntity<Boolean> addPermission(@RequestBody @Validated CreatePermissionDto dto) {
        return ResponseEntity.ok(roleService.createPermission(dto));
    }

    @GetMapping("permissions")
    @Operation(summary = "获取权限列表")
    @PreAuthorize("hasAuthority('role:permissions-list')")
    public ResponseEntity<List<PermissionVo>> getAllPermissions() {
        return ResponseEntity.ok(roleService.getAllPermissions());
    }

    @PostMapping(value = "set-role-permission", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "设置角色权限")
    @PreAuthorize("hasAuthority('role:set-role-permission')")
    public ResponseEntity<Integer> setRolePermissions(@RequestBody @Validated SetRolePermissionDto dto) {
        return ResponseEntity.ok(roleService.setRolePermissions(dto));
    }

}
