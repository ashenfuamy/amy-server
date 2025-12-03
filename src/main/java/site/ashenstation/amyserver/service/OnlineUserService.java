package site.ashenstation.amyserver.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.ashenstation.amyserver.config.properties.SecurityProperties;
import site.ashenstation.amyserver.dto.OnlineUserDto;
import site.ashenstation.amyserver.utils.EncryptUtils;
import site.ashenstation.amyserver.utils.IpAddrUtils;
import site.ashenstation.amyserver.utils.RedisUtils;
import site.ashenstation.amyserver.utils.TokenProvider;
import site.ashenstation.amyserver.utils.enums.LoginPlatform;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class OnlineUserService {
    private final SecurityProperties securityProperties;
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    public void save(String username, String token, HttpServletRequest request) {
        String ip = IpAddrUtils.getIp(request);
        String address = IpAddrUtils.getCityInfo(ip);
        OnlineUserDto onlineUserDto;

        try {
            onlineUserDto = new OnlineUserDto();
            onlineUserDto.setIp(ip);
            onlineUserDto.setUserName(username);
            onlineUserDto.setAddress(address);

            onlineUserDto.setUid(EncryptUtils.desEncrypt(token));
            onlineUserDto.setLoginTime(new Date());

            onlineUserDto.setLoginPlatform(LoginPlatform.find(request.getHeader(securityProperties.getClientHeader())));

            String loginKey = tokenProvider.loginKey(token, onlineUserDto.getLoginPlatform());

            redisUtils.set(loginKey, onlineUserDto, securityProperties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUserDto getOne(String key) {
        return redisUtils.get(key, OnlineUserDto.class);
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    public void kickOutForUsername(String username) {
        String loginKey = securityProperties.getOnlineKey() + username + "*";
        redisUtils.scanDel(loginKey);
    }

    public void kickOutForUsernameAndPlatform(String username, LoginPlatform platform) {
        String loginKey = securityProperties.getOnlineKey() + platform.getType() + ":" + username + "*";
        redisUtils.scanDel(loginKey);
    }
}
