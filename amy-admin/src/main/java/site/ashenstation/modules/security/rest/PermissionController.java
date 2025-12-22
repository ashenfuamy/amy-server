package site.ashenstation.modules.security.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.modules.security.dto.PermissionDto;
import site.ashenstation.modules.security.dto.SetPermissionDto;
import site.ashenstation.modules.security.service.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permission")
@Tag(name = "权限管理", description = "权限管理相关接口")
public class PermissionController {

    private final AdminService adminService;


    @PostMapping("add-permission")
    @Operation(summary = "添加权限")
    public ResponseEntity<Integer> add(@RequestBody @Validated PermissionDto dto) {
        return ResponseEntity.ok(adminService.addPermission(dto));
    }

    @PostMapping("set-permission")
    @Operation(summary = "设置用户权限")
    public ResponseEntity<?> setPermission(@RequestBody @Validated SetPermissionDto dto) {
        return ResponseEntity.ok(adminService.setPermission(dto));
    }


}
