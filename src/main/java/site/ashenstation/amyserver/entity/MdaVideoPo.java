package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Table("mda_video")
public class MdaVideoPo {
    @Id
    private String id;
    private String summaryId;
    private String publisherId;
    private Long size;
    private String filePath;
    private String parentFilePath;
    private String relativePath;
    private Boolean converted;

    @RelationOneToOne(targetField = "id", selfField = "summaryId")
    private MdaSummaryPo summary;
    @RelationOneToOne(targetField = "id", selfField = "publisherId")
    private MdaPublisherPo publisher;

    @RelationManyToMany(
            joinTable = "mda_actor_video_summary",
            selfField = "id",
            joinSelfColumn = "actor_id",
            targetField = "id",
            joinTargetColumn = "video_summary_id"
    )
    private List<ActorPo> actors;
}
