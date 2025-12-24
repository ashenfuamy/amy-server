package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("mda_publisher")
public class MdaPublisher {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String name;
}
