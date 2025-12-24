package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Table("mda_summary_tag")
@Data
public class MdaSummaryTag {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer summaryId;
    private Integer tagId;
}
