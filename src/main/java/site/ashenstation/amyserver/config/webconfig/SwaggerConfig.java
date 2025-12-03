package site.ashenstation.amyserver.config.webconfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class SwaggerConfig {
    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        // 配置服务器信息
        Server devServer = new Server();
        devServer.setUrl("http://localhost:" + serverPort);
        devServer.setDescription("开发环境");

        // 配置文档信息
        Contact contact = new Contact()
                .name("ASHEN")
                .email("707978562@qq.com");

        Info info = new Info()
                .title("AMY_SERVER API文档")
                .version("1.0.0")
                .contact(contact)
                .description("这是基于SpringDoc生成的API接口文档");

        log.info("Swagger Initializing, Doc URL: {}", devServer.getUrl() + "/swagger-ui.html");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }

}
