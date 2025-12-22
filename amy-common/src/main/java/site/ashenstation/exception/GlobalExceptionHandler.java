package site.ashenstation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandler {

    /**
     * 处理请求体校验失败
     *
     * @param ex 异常
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // 收集所有错误信息
                .map(DefaultMessageSourceResolvable::getDefaultMessage) // 收集所有错误信息
                .toList();

        // 打印堆栈信息
//        log.error(ex.getMessage());
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST, errors.getFirst()));

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return GlobalExceptionHandler.buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    public static ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
