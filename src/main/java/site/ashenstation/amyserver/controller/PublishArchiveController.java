package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.dto.PublishArchiveDto;
import site.ashenstation.amyserver.service.PublishArchiveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version")
@Tag(name = "应用版本管理", description = "所有应用版本管理的接口")
public class PublishArchiveController {

    private final PublishArchiveService publishArchiveService;

    @PostMapping("publish")
    public ResponseEntity<Boolean> publish(PublishArchiveDto dto) {
        return ResponseEntity.ok(publishArchiveService.publish(dto));
    }
}
