package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Table("mda_actor")
public class ActorPo {
    @Id
    private String id;
    private String name;
    private String introduction;
    private String avatarPath;
    private String avatarName;
    private String creatorId;
    private Date createTime;
    @Column("tag")
    private String tagId;



    @RelationOneToOne(targetField = "id", selfField = "tagId")
    private ActorTagPo tag;
}
