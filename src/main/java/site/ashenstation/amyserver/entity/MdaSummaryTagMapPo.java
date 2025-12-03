package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("mda_summary_tag")
public class MdaSummaryTagMapPo {
    @Id
    private String id;
    private String summaryId;
    private String tagId;
}
