package site.ashenstation.amyserver;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@MapperScan("site.ashenstation.amyserver.mapper")
public class AmyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmyServerApplication.class, args);
    }

}
