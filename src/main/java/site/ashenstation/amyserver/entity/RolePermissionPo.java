package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("sys_role_permission")
public class RolePermissionPo {
    @Id
    private String id;
    private String roleId;
    private String permissionId;
}
