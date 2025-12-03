package site.ashenstation.amyserver.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Configuration
public class AuthenticationExceptionHandler {


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        log.error(ex.getMessage());
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledExceptionHandler(DisabledException ex) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> lockedExceptionHandler(LockedException ex) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<?> accountExpiredExceptionHandler(AccountExpiredException ex) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException ex) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }
}
