package site.ashenstation.amyserver.dto;

import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;

@Data
@ToString
public class RoleDto {
    @NotBlank(message = "角色名不能为空")
    private String name;
    private String description;
    private Boolean enabled;
}
