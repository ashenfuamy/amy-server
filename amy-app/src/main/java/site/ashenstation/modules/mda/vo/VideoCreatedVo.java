package site.ashenstation.modules.mda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoCreatedVo {
    private Integer videoId;
    private Integer summaryId;
    private Integer status;
}
