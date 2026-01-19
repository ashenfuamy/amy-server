package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.enums.UploadStatus;
import site.ashenstation.modules.mda.dto.CreateVideoDto;
import site.ashenstation.modules.mda.dto.UploadChunkDto;
import site.ashenstation.modules.mda.service.VideoService;
import site.ashenstation.modules.mda.vo.VideoTaskVo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("create")
    private ResponseEntity<VideoTaskVo> create(CreateVideoDto dto) {
        return ResponseEntity.ok(videoService.createVideo(dto));
    }


    @PostMapping("chunk")
    private void uploadChunk(UploadChunkDto dto) {
        videoService.uploadChunk(dto);
    }

    @GetMapping("upload-status")
    private ResponseEntity<UploadStatus> queryUploadStatus(String id) {
        return ResponseEntity.ok(videoService.queryUploadStatus(id));
    }

    @GetMapping("finish")
    private void uploadFinish(String id) {
        videoService.finishUpload(id);
    }
}
