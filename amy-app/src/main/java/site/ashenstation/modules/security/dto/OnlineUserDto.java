package site.ashenstation.modules.security.dto;

import lombok.Data;
import site.ashenstation.enums.LoginPlatform;

import java.util.Date;

@Data
public class OnlineUserDto {
    private String uid;

    private String userName;
    private String ip;

    private String address;

    private String key;

    private Date loginTime;

    private LoginPlatform loginPlatform;
}
