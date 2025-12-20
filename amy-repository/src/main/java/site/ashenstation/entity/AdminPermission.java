package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Table("sys_admin_permission")
@Data
@ToString
public class AdminPermission {
    private Integer id;
    private Integer permissionId;
    private Integer adminId;
}
