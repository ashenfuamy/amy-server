package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.base.BaseUser;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Table("sys_user")
public class User extends BaseUser {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private Boolean enabled;
    private String phoneNumber;
}
