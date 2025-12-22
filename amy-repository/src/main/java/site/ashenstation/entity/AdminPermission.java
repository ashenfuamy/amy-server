package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table("sys_admin_permission")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminPermission {
    private Integer id;
    private Integer permissionId;
    private Integer adminId;

    public AdminPermission(Integer permissionId, Integer adminId) {
        this.permissionId = permissionId;
        this.adminId = adminId;
    }
}
