package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table("sys_user_resource_permission")
@Data
@NoArgsConstructor
public class UserResourcePermission {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer userId;
    private String resourceId;
    private Date expireTimestamp;



    public UserResourcePermission(Integer userId, String resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
    }
}
