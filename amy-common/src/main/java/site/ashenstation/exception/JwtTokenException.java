package site.ashenstation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtTokenException extends RuntimeException {

    private final Integer status = HttpStatus.UNAUTHORIZED.value();
    private final String message = "Token 令牌已过期！";

    public JwtTokenException() {
        super();
    }
}
