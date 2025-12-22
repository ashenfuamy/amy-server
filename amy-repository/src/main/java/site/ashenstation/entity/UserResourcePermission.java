package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.Date;

@Table("sys_user_resource_permission")
@Data
public class UserResourcePermission {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer userId;
    private Integer resourceId;
    private Date expireTimestamp;
}
