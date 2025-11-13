package site.ashenstation.amyserver.config.mybatisflex;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MyBatisFlexConfig {

    private final SimpleUUIDKeyGenerator simpleUUIDKeyGenerator;

    @PostConstruct
    public void registerKeyGenerator() {
        // 将生成器注册到工厂，并为其命名，例如 "myCustom"
        KeyGeneratorFactory.register("simpleUuid", simpleUUIDKeyGenerator);

        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(com.mybatisflex.annotation.KeyType.Generator);

        keyConfig.setValue("simpleUuid");
        keyConfig.setBefore(true);

        FlexGlobalConfig.getDefaultConfig().setKeyConfig(keyConfig);

        //开启审计功能
        AuditManager.setAuditEnable(true);
        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{},{}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );
    }
}
