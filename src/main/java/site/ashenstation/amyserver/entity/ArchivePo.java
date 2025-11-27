package site.ashenstation.amyserver.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.amyserver.utils.enums.ArchiveStatus;

import java.util.Date;

@Data
@ToString
@Table("sys_archive")
public class ArchivePo {
    @Id
    private String id;
    private String name;
    private String version;
    private String directoryPath;
    private String describe;
    private Date publishTime;
    private ArchiveStatus status;
    private Boolean isLatest;
}
