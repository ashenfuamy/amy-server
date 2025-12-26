package site.ashenstation.modules.mda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaTag;

import java.util.List;

@Data
@ToString
public class CreateVideoDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String subtitle;
    private Integer mosaicType;
    private String serialNumber;
    private MdaPublisher publisher;
    @NotNull(message = "海报不能为空")
    private MultipartFile posterFile;
    @NotEmpty(message = "参演不能为空")
    private List<Integer> actors;

    private List<MdaTag> tags;

    private Long size;
    private Integer serialId;
}
