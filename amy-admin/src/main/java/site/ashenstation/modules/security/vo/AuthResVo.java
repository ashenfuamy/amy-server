package site.ashenstation.modules.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.ashenstation.entity.Admin;

@Data
@AllArgsConstructor
public class AuthResVo {
    private String token;
    private Admin admin;
}
