package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.amyserver.utils.enums.RoleDataScope;

import java.util.Date;

@Data
@ToString
@Table("sys_role")
public class RolePo {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean enabled;
    private Date createAt;
    private Date updateAt;
    private RoleDataScope dataScope;
}
