package site.ashenstation.amyserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SetUserRolesDto {
    @NotBlank(message = "用户不能为空")
    private String userId;
    private List<String> roleId;
}
