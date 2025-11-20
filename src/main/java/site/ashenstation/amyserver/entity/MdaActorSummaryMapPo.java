package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("mda_actor_video_summary")
public class MdaActorSummaryMapPo {
    @Id
    private String id;
    private String actorId;
    private String summaryId;
}
