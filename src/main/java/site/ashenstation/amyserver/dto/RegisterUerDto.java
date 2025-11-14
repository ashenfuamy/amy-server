package site.ashenstation.amyserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterUerDto {
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @Schema(description = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @Schema(description = "邮箱地址")
    private String email;
    @Schema(description = "手机号")
    private String phoneNumber;
}
