package site.ashenstation.amyserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SetRolePermissionDto {
    @NotBlank(message = "角色不能为空")
    private String roleId;
    private List<String> permissionId;
}
