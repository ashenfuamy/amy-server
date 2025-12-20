package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.base.BaseUser;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Table("sys_admin")
public class Admin extends BaseUser {
    @Id(keyType = KeyType.Auto)
    private Integer id;
}
