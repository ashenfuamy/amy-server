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
@Table("sys_user")
public class UserPo {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String avatarPath;
    private Date createdAt;
    private Date updatedAt;
    private Date lastLogin;
    private Boolean locked;
    private Boolean enabled;
    private String phoneNumber;

    @RelationManyToMany(
            joinTable = "sys_user_role",
            selfField = "id",
            joinSelfColumn = "user_id",
            targetField = "id",
            joinTargetColumn = "role_id"
    )
    private List<RolePo> role;
}
