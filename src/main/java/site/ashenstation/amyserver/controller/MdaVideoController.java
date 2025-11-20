package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.amyserver.service.MdaVideoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@Tag(name = "媒体视频操作", description = "所有媒体视频操作的接口")
public class MdaVideoController {

    private final MdaVideoService mdaVideoService;
}
