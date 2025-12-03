package site.ashenstation.amyserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.entity.ActorTagPo;
import site.ashenstation.amyserver.entity.MdaTagPo;

@Data
@ToString
public class CreateActorDto {
    @NotBlank(message = "演员名称不能为空")
    private String name;
    @NotNull(message = "演员头像不能为空")
    private MultipartFile avatarFile;
    private String introduction;
    @NotNull(message = "演员标签不能为空")
    private ActorTagPo tag;
}
