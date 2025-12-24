package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Table("mda_actor_video_summary")
@Data
@ToString
public class MdaSummaryActorMap {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer actorId;
    private Integer summaryId;
}
