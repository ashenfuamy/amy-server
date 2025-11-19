package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("sys_permission")
public class PermissionPo {
    @Id
    private String id;
    private String name;
    private String code;
    private String field;
}
