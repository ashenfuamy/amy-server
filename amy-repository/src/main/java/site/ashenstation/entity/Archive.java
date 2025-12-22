package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.enums.ArchiveStatus;

import java.util.Date;

@Table("sys_archive")
@Data
@ToString
public class Archive {
    @Id(keyType = KeyType.Auto)
    private String id;
    private String name;
    private String version;
    private String directoryPath;
    private String describe;
    private String platform;
    private String arch;
    private Date publishTime;
    private ArchiveStatus status;
    private Boolean isLatest;
}
