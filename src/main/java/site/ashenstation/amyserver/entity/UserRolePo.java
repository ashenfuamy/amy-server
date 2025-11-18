package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("sys_user_role")
public class UserRolePo {
    @Id
    private String id;
    private String userId;
    private String roleId;
}
