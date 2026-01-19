package site.ashenstation.modules.mda.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class UploadChunkDto {
    private String id;
    private MultipartFile chunk;
    private Integer currentChunk;
}
