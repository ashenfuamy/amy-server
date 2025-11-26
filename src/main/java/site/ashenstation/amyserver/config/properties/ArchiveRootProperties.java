package site.ashenstation.amyserver.config.properties;

import lombok.Data;
import lombok.ToString;
import site.ashenstation.amyserver.utils.enums.ArchiveType;

@Data
@ToString
public class ArchiveRootProperties {
    private ArchiveType name;
}
