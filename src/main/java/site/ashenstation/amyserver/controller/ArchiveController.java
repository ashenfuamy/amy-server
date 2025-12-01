package site.ashenstation.amyserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.ashenstation.amyserver.dto.PublishArchiveDto;
import site.ashenstation.amyserver.dto.SetArchiveLatestDto;
import site.ashenstation.amyserver.service.ArchiveService;
import site.ashenstation.amyserver.utils.annotation.rest.AnonymousPostMapping;
import site.ashenstation.amyserver.vo.ArchiveVo;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "应用版本管理", description = "所有应用版本管理的接口")
public class ArchiveController {

    private final ArchiveService publishArchiveService;

    @AnonymousPostMapping("/api/version/publish")
    @Operation(summary = "版本发布")
    public ResponseEntity<Boolean> publish(PublishArchiveDto dto) {
        return ResponseEntity.ok(publishArchiveService.publish(dto));
    }

    @GetMapping("/getVersionList")
    @Operation(summary = "获取版本列表")
    public ResponseEntity<List<ArchiveVo>> getVersionList(String appName) {
        return ResponseEntity.ok(publishArchiveService.getVersionList(appName));
    }


    @PostMapping("set-latest-version")
    @Operation(summary = "设置最新版本")
    public ResponseEntity<Boolean> setLatestVersion(SetArchiveLatestDto dto) {
        return ResponseEntity.ok(publishArchiveService.setLatest(dto));
    }

    @GetMapping("/amy/version/{platform}/{_arch}/{archive}")
    @Operation(summary = "获取最新版本路径")
    public void path(@PathVariable String platform, @PathVariable String _arch, @PathVariable String archive, HttpServletResponse response) throws IOException {
        response.sendRedirect(publishArchiveService.getLatestArchivePath(platform, _arch, "amy", archive));
    }
}
