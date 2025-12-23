package site.ashenstation.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String avatarRoot;
    private String posterRoot;
    private String publishResourceRoot;
    private List<VideoRootProperties> videoRoots;

    private String imageExt;
    private String enableVideoRoot;
    private String uploadCacheKey;

    private String avatarResourcePrefix;
    private String posterResourcePrefix;
    private String videoResourcePrefix;
    private String publishResourcePrefix;

    public String getVideoRoot() {
        for (VideoRootProperties videoRoot : videoRoots) {
            if (videoRoot.getName().equals(this.enableVideoRoot)) {
                return videoRoot.getPath();
            }
        }

        return null;
    }
}