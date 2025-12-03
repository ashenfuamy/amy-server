package site.ashenstation.amyserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.entity.UserPo;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateUserDto extends UserPo {

    private MultipartFile avatarFile;
}
