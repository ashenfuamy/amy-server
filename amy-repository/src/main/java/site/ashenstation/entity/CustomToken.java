package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Table("sys_custom_token")
public class CustomToken {
    @Id(keyType = KeyType.Auto)
    private String id;
    private String uid;
    private String title;
    private String token;
    private Date createTime;
    private Integer creator;
}
