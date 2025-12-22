package site.ashenstation.modules.appuser.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class CreateUserDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Boolean enabled;
    private MultipartFile avatar;
}
