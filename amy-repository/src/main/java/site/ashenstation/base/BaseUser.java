package site.ashenstation.base;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public abstract class BaseUser {
    private String username;
    private String password;
    private String avatarPath;
    private Date lastLogin;
    private Boolean locked;
}
