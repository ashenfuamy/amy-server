package site.ashenstation.modules.security.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import site.ashenstation.modules.security.dto.JwtUserDto;
import site.ashenstation.properties.LoginProperties;
import site.ashenstation.utils.RedisUtils;

@Service
@RequiredArgsConstructor
public class UserCacheManager {
    private final RedisUtils redisUtils;
    private final LoginProperties loginProperties;

    /**
     * 返回用户缓存
     *
     * @param userName 用户名
     * @return JwtUserDto
     */
    public JwtUserDto getUserCache(String userName) {
        if (StrUtil.isNotEmpty(userName)) {
            // 获取数据
            Object obj = redisUtils.get(LoginProperties.cacheKey + userName);
            if (obj != null) {
                return (JwtUserDto) obj;
            }
        }
        return null;
    }

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    public void addUserCache(String userName, JwtUserDto user) {
        if (StrUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期
            long time = loginProperties.getUserCacheIdleTime() + RandomUtil.randomInt(900, 1800);
            redisUtils.set(LoginProperties.cacheKey + userName, user, time);
        }
    }

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    public void cleanUserCache(String userName) {
        if (StrUtil.isNotEmpty(userName)) {
            // 清除数据
            redisUtils.del(LoginProperties.cacheKey + userName);
        }
    }
}
