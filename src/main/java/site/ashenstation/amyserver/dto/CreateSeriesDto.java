package site.ashenstation.amyserver.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.entity.MdaPublisherPo;
import site.ashenstation.amyserver.entity.MdaTagPo;
import site.ashenstation.amyserver.utils.enums.MosaicType;

import java.util.List;

@Data
@ToString
public class CreateSeriesDto {
    private String title;
    private MosaicType mosaicType;
    private String serialNumber;
    private MultipartFile posterFile;
    private String actorId;
    private MdaPublisherPo publisher;
    private List<MdaTagPo> tags;
}
