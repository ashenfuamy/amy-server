package site.ashenstation.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Table("mda_actor")
public class Actor {
    private Integer id;
    private String name;
    private Integer tagId;
    private String Introduction;
    private String avatarPath;
    private String creatorId;
    @Column("create_timestamp")
    private Date createTime;
    private String country;
    private String website;

    @RelationOneToOne(selfField = "tagId", targetField = "id")
    private ActorTag tag;
}
