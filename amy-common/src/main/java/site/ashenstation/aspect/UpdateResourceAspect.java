package site.ashenstation.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.SecurityUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class UpdateResourceAspect {
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    @Pointcut("@annotation(site.ashenstation.annotation.UpdateResourceCache)")
    public void updatePointcut() {}


    @AfterReturning("updatePointcut()")
    public void doUpdate() {
        String currentUsername = SecurityUtils.getCurrentUsername();
        String key = securityProperties.getResourcePermissionKey() + currentUsername;
        redisUtils.del(key);
    }

}
