package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.ashenstation.amyserver.dto.PublishArchiveDto;
import site.ashenstation.amyserver.service.PublishArchiveService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Tag(name = "应用版本管理", description = "所有应用版本管理的接口")
public class PublishArchiveController {

    private final PublishArchiveService publishArchiveService;

    @PostMapping("/api/version/publish")
    public ResponseEntity<Boolean> publish(PublishArchiveDto dto) {
        return ResponseEntity.ok(publishArchiveService.publish(dto));
    }

    @GetMapping("/amy/version/{platform}/{_arch}/{archive}")
    public void path(@PathVariable String platform, @PathVariable String _arch, @PathVariable String archive, HttpServletResponse response) throws IOException {
        System.out.println(platform);
        System.out.println(_arch);
        System.out.println(archive);

        response.sendRedirect("/static/arch/1.0.1/" + archive);
    }
}
