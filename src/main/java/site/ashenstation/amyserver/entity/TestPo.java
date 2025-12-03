package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table("sys_test")
public class TestPo {
    @Id
    private String id;
    private Integer testInt;
    private Boolean testBoolean;
}
