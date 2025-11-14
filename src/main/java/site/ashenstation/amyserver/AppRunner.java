package site.ashenstation.amyserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.utils.annotation.rest.AnonymousGetMapping;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@MapperScan("site.ashenstation.amyserver.mapper")
@RestController
public class AppRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AppRunner.class);
        // 监控应用的PID，启动时可指定PID路径：--spring.pid.file=/home/eladmin/app.pid
        // 或者在 application.yml 添加文件路径，方便 kill，kill `cat /home/eladmin/app.pid`
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }


    @AnonymousGetMapping("/")
    public String index() {
        return "WELCOME TO AMY APP SERVER";
    }
}
