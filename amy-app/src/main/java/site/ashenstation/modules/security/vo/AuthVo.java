package site.ashenstation.modules.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import site.ashenstation.entity.User;

@AllArgsConstructor
@Data
public class AuthVo {
    private String token;
    private User user;
}
