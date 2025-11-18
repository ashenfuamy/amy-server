package site.ashenstation.amyserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.amyserver.utils.enums.RoleDataScope;
import jakarta.validation.constraints.NotBlank;

@Data
@ToString
public class RoleDto {
    @NotBlank(message = "角色名不能为空")
    private String name;
    private String description;
    @NotNull(message = "角色数据范围不能为空")
    private RoleDataScope dataScope;
    private Boolean enabled;
}
