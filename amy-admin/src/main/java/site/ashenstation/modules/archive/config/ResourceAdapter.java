package site.ashenstation.modules.archive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.ashenstation.modules.archive.config.properties.ArchiveRepositoryProperties;
import site.ashenstation.properties.FileProperties;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ResourceAdapter implements WebMvcConfigurer {
    private final ArchiveRepositoryProperties archiveRepositoryProperties;
    private final FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String archive = "file:" + archiveRepositoryProperties.getRoot().replace("\\", "/") + "/";
        String publicResource = "file:" + fileProperties.getPublishResourceRoot().replace("\\", "/") + "/";

        String avatarUtl = "file:" + fileProperties.getAvatarRoot().replace("\\", "/") + "/";
        String posterUtl = "file:" + fileProperties.getPosterRoot().replace("\\", "/") + "/";

        registry.addResourceHandler(archiveRepositoryProperties.getUriPath() + "/**").addResourceLocations(archive);
        registry.addResourceHandler(fileProperties.getPublishResourcePrefix() + "/**").addResourceLocations(publicResource);

        registry.addResourceHandler(fileProperties.getAvatarResourcePrefix() + "/**").addResourceLocations(avatarUtl);
        registry.addResourceHandler(fileProperties.getPosterResourcePrefix() + "/**").addResourceLocations(posterUtl);

    }
}
