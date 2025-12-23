package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;


@Table("mda_actor_tag")
@Data
@ToString
public class ActorTag {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String title;
}
