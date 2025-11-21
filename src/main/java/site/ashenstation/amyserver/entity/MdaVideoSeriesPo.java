package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("mda_video_series")
public class MdaVideoSeriesPo {
    @Id
    private String id;
    @Column("summary_id")
    private String summaryId;
    @Column("publisher_id")
    private String publisherId;

    @RelationOneToOne(targetField = "id", selfField = "summaryId")
    private MdaSummaryPo Summary;
    @RelationOneToOne(targetField = "id", selfField = "publisherId")
    private MdaPublisherPo publisher;
}
