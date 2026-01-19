package site.ashenstation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import site.ashenstation.properties.VideoTranscodingTaskMQProperties;

@Component
@RequiredArgsConstructor
public class RabbitMQForVideoTranscodingTaskConfig {
    private final VideoTranscodingTaskMQProperties messageQueueProperties;

    @Bean
    public Queue queue() {
        return new Queue(messageQueueProperties.getQueueName(), true); // 第二个参数true表示队列持久化
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(messageQueueProperties.getExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(messageQueueProperties.getRoutingKey()); // 设置路由键
    }
}
