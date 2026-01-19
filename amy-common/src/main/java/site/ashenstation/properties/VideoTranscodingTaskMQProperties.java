package site.ashenstation.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "video-task-mq")
public class VideoTranscodingTaskMQProperties {
    private String queueName;
    private String exchangeName;
    private String routingKey;
}
