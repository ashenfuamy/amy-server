package site.ashenstation.amyserver.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "archive-repo")
public class ArchiveRepositoryProperties {
    private List<ArchiveRootProperties> archiveRoot;
}

