package site.ashenstation.modules.archive.config.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "archive-repo")
public class ArchiveRepositoryProperties {
    private String root;
    private String uriPath;
    private String amyArchiveDirectory;


    @PostConstruct
    public void afterPropertiesSet() throws FileNotFoundException {
        File rootFile = new File(root);
        if (!rootFile.exists()) {
            throw new FileNotFoundException("应用产物仓库根目录不存在，无法完成初始化！");
        }

        File amyDirectory = new File(root, amyArchiveDirectory);
        if (!amyDirectory.exists()) {
            amyDirectory.mkdir();
        }
    }
}
