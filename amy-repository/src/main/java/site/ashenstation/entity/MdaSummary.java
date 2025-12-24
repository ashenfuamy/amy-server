package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.enums.MdaSummaryType;
import site.ashenstation.enums.MosaicType;

import java.util.Date;

@Table("mda_summary")
@Data
@ToString
public class MdaSummary {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String title;
    private String subtitle;
    private MosaicType mosaicType;
    private MdaSummaryType type;
    private String serialNumber;
    private String posterUrl;
    private Date createdAt;
    private Integer publisherId;
}
