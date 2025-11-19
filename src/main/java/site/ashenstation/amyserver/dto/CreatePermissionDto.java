package site.ashenstation.amyserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreatePermissionDto {

    @NotBlank(message = "权限名不能为空")
    private String name;
    @NotBlank(message = "权限编码不能为空")
    private String code;
    @NotBlank(message = "权限范围不能为空")
    private String field;
}
