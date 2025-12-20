package site.ashenstation.modules.security.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.modules.security.dto.AuthenticationDto;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    @AnonymousPostMapping("login")
    public ResponseEntity<HashMap<Object, Object>> login(@RequestBody @Valid AuthenticationDto dto, HttpServletRequest request) throws Exception {
        return null;
    }
}
