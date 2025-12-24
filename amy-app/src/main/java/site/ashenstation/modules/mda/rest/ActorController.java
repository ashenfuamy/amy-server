package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.entity.ActorTag;
import site.ashenstation.modules.mda.dto.CreateActorDto;
import site.ashenstation.modules.mda.service.ActorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actor")
public class ActorController {

    private final ActorService actorService;

    @PostMapping("create")
    public ResponseEntity<Boolean> createActor(CreateActorDto dto) {
        return ResponseEntity.ok(actorService.createActor(dto));
    }


    @GetMapping("tags")
    public ResponseEntity<List<ActorTag>> getActorTags() {
        return ResponseEntity.ok(actorService.getActorTags());
    }

    @GetMapping("list-by-classify-tag")
    public ResponseEntity<Boolean> getListByClassifyTag() {
        return ResponseEntity.ok(actorService.getActorListByClassifyTag());
    }
}
