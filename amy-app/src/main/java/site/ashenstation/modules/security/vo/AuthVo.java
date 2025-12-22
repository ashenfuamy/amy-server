package site.ashenstation.modules.security.vo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import site.ashenstation.entity.User;

@AllArgsConstructor
public class AuthVo {
    private String token;
    private User user;
}
