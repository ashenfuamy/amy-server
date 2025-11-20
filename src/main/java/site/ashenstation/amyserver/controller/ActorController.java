package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.dto.CreateActorDto;
import site.ashenstation.amyserver.service.ActorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actor")
@Tag(name = "创作者管理", description = "所有关于艺术家操作的接口")
public class ActorController {

    private final ActorService actorService;

    @PostMapping("add")
    @Operation(summary = "添加创作者")
    public ResponseEntity<Boolean> createActor(CreateActorDto dto) {
        return ResponseEntity.ok(actorService.createActor(dto));
    }
}
