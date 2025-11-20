package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("mda_actor_tag")
public class ActorTagPo {
    @Id
    private String id;
    private String title;
}
