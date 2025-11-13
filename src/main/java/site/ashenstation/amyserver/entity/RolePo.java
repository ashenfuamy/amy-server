package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("sys_role")
public class RolePo {
    @Id
    private String id;
    private String name;
    private String description;
    private String enabled;
    private Long createAt;
    private Long updateAt;
    private Long dataScope;
}
