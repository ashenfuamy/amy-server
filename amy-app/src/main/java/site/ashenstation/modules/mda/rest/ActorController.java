package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.modules.mda.dto.CreateActorDto;
import site.ashenstation.modules.mda.service.ActorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actor")
public class ActorController {

    private final ActorService actorService;

    @PostMapping("create")
    private ResponseEntity<Boolean> createActor(CreateActorDto dto) {
        return ResponseEntity.ok(actorService.createActor(dto));
    }
}
