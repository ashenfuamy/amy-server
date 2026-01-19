package site.ashenstation.modules.mda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VideoTranCodingDto {
    private String TaskId;
    private Integer userId;
}
