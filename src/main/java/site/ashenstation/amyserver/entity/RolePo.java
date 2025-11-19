package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Table("sys_role")
public class RolePo {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
    private Boolean enabled;
    private Date createAt;
    private Date updateAt;

    @RelationManyToMany(
            joinTable = "sys_role_permission",
            selfField = "id",
            joinSelfColumn = "role_id",
            targetField = "id",
            joinTargetColumn = "permission_id"
    )
    private List<PermissionPo> permissions;
}
