package site.ashenstation.modules.mda.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.ActorTag;

@Data
@ToString
public class CreateActorDto {
    private String name;
    private String Introduction;
    private String country;
    private String website;
    private MultipartFile avatarFile;
    private ActorTag actorTag;
}
