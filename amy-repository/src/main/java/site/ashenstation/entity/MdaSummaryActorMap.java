package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table("mda_actor_video_summary")
@Data
@ToString
@NoArgsConstructor
public class MdaSummaryActorMap {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer actorId;
    private Integer summaryId;

    public MdaSummaryActorMap(Integer actorId, Integer summaryId) {
        this.actorId = actorId;
        this.summaryId = summaryId;
    }
}
