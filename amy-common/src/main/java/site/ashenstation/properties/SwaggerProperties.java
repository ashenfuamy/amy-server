package site.ashenstation.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact = new Contact();


    @Setter
    @Getter
    public static class Contact {
        private String name;
        private String url;
        private String email;

    }
}
