package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Table("mda_resource_permission")
@NoArgsConstructor
public class ResourcePermissionPo {
    @Id
    private String id;
    private String userId;
    private String resourceId;
    private Date expired;

    public ResourcePermissionPo(String userId, String resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
    }
}
