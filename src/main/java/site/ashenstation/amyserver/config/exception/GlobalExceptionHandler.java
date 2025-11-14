package site.ashenstation.amyserver.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST, errors.getFirst()));

    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
