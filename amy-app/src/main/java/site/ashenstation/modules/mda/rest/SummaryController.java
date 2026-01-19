package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaTag;
import site.ashenstation.modules.mda.service.SummaryService;
import site.ashenstation.modules.security.vo.ActorSummariesVo;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("publishers")
    public ResponseEntity<List<MdaPublisher>> getPublishers() {
        return ResponseEntity.ok(summaryService.getPublishers());
    }

    @GetMapping("tags")
    public ResponseEntity<List<MdaTag>> getMdaTags() {
        return ResponseEntity.ok(summaryService.getSummaryTags());
    }

    @GetMapping("list")
    public ResponseEntity<List<ActorSummariesVo>> getMdaSummaries(Integer actorId) {
        return ResponseEntity.ok(summaryService.getSummaryTagsByActor(actorId));
    }

    @GetMapping("updateResourcePermissionCache")
    public void updateResourcePermissionCache() {
        summaryService.updateResourcePermissionCache();
    }


}
