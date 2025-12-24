package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.entity.ActorTag;
import site.ashenstation.modules.mda.dto.ActorDto;
import site.ashenstation.modules.mda.service.ActorService;
import site.ashenstation.modules.mda.vo.ActorListByClassifyTagVo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actor")
public class ActorController {

    private final ActorService actorService;

    @PostMapping("create")
    public ResponseEntity<Boolean> createActor(ActorDto dto) {
        return ResponseEntity.ok(actorService.createActor(dto));
    }

    @PostMapping("update")
    public ResponseEntity<Boolean> updateActor(ActorDto dto) {
        return ResponseEntity.ok(actorService.updateActor(dto));
    }

    @PostMapping("update-avatar")
    public ResponseEntity<Boolean> updateActorAvatar(ActorDto dto) {
        return ResponseEntity.ok(actorService.updateActorAvatar(dto));
    }

    @DeleteMapping("remove")
    public ResponseEntity<Integer> removeActor(Integer id) {
        return ResponseEntity.ok(actorService.deleteActor(id));
    }

    @GetMapping("tags")
    public ResponseEntity<List<ActorTag>> getActorTags() {
        return ResponseEntity.ok(actorService.getActorTags());
    }

    @GetMapping("list-by-classify-tag")
    public ResponseEntity<List<ActorListByClassifyTagVo>> getListByClassifyTag() {
        return ResponseEntity.ok(actorService.getActorListByClassifyTag());
    }
}
