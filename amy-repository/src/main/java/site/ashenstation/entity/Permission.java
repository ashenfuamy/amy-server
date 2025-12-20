package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("sys_permission")
public class Permission {
    private Integer id;
    private String title;
    private String code;
    private String field;
}
