package site.ashenstation.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.ashenstation.properties.SwaggerProperties;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(swaggerProperties.getTitle())
                                .description(swaggerProperties.getDescription())
                                .version(swaggerProperties.getVersion())
                                .contact(
                                        new Contact()
                                                .name(swaggerProperties.getContact().getName())
                                                .email(swaggerProperties.getContact().getEmail())
                                                .url(swaggerProperties.getContact().getUrl())
                                )
                );
    }
}
