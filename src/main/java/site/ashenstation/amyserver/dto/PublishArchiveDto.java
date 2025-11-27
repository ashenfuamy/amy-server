package site.ashenstation.amyserver.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
public class PublishArchiveDto {
    private String platform;
    private String arch;
    private String name;
    private String version;
    private Boolean replaceExisting;
    private List<MultipartFile> files;
}
