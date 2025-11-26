package site.ashenstation.amyserver.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "archive-repo")
public class ArchiveRepositoryProperties {
    private ArchiveProperties amyArchive;
}

