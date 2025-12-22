package site.ashenstation.modules.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PermissionDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "编码不能为空")
    private String code;
    @NotBlank(message = "field 不能为空")
    private String field;
}
