package site.ashenstation.modules.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.ashenstation.properties.FileProperties;

import java.io.File;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ResourceAdapter implements WebMvcConfigurer {
    private final FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String publicResource = "file:" + fileProperties.getPublishResourceRoot().replace("\\", "/") + "/";
        String avatarUtl = "file:" + fileProperties.getAvatarRoot().replace("\\", "/") + "/";
        String posterUtl = "file:" + fileProperties.getPosterRoot().replace("\\", "/") + "/";

        registry.addResourceHandler(fileProperties.getPublishResourcePrefix() + "/**").addResourceLocations(publicResource);

        registry.addResourceHandler(fileProperties.getAvatarResourcePrefix() + "/**").addResourceLocations(avatarUtl);
        registry.addResourceHandler(fileProperties.getPosterResourcePrefix() + "/**").addResourceLocations(posterUtl);

        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(fileProperties.getVideoResourcePrefix() + "/**");
        fileProperties.getVideoRoots().forEach(vr -> {
            File file = new File(vr.getPath());

            resourceHandlerRegistration.addResourceLocations("file:" + file.getAbsolutePath().replace("\\", "/") + "/");
        });
    }
}
