package site.ashenstation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousGetMapping;
import site.ashenstation.utils.SpringBeanHolder;

@SpringBootApplication
@Slf4j
@MapperScan("site.ashenstation.mapper")
@RestController
@EnableTransactionManagement
@RequiredArgsConstructor
public class AppRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AppRunner.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

    @Bean("appSpringContextHolder")
    public SpringBeanHolder springContextHolder() {
        return new SpringBeanHolder();
    }

    @AnonymousGetMapping("/")
    public String index() {
        return "WELCOME TO AMY APP CLIENT SERVER";
    }
}
