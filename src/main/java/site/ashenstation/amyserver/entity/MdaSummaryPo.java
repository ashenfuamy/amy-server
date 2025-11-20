package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.amyserver.utils.enums.MdaSummaryType;
import site.ashenstation.amyserver.utils.enums.MosaicType;

import java.util.Date;

@Data
@ToString
@Table("mda_summary")
public class MdaSummaryPo {
    @Id
    private String id;
    private String title;
    private String serialNumber;
    private String posterUrl;
    private String posterName;
    private Long duration;
    private String seriesId;
    private Date createAt;
    private MdaSummaryType type;
    private MosaicType mosaicType;
}
