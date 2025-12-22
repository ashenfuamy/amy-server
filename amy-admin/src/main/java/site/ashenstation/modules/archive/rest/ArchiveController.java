package site.ashenstation.modules.archive.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.modules.archive.dto.PublishArchiveDto;
import site.ashenstation.modules.archive.dto.SetArchiveLatestDto;
import site.ashenstation.modules.archive.service.AmyArchiveService;
import site.ashenstation.modules.archive.vo.ArchiveVo;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "应用版本管理", description = "所有应用版本管理的接口")
public class ArchiveController {

    private final AmyArchiveService amyArchiveService;

    @AnonymousPostMapping("/api/version/publish")
    @Operation(summary = "版本发布")
    @PreAuthorize("hasAuthority('archive:publish')")
    public ResponseEntity<Boolean> publish(PublishArchiveDto dto) {
        return ResponseEntity.ok(amyArchiveService.publish(dto));
    }

    @GetMapping("/api/version/get-version-list")
    @Operation(summary = "获取版本列表")
    @PreAuthorize("hasAuthority('archive:get-version-list')")
    public ResponseEntity<List<ArchiveVo>> getVersionList(String appName, String platform, String arch) {
        return ResponseEntity.ok(amyArchiveService.getVersionList(appName, platform, arch));
    }

    @GetMapping("/get-new-version")
    @Operation(summary = "获取最新发布的版本")
    @PreAuthorize("hasAuthority('archive:get-new-version')")
    public ResponseEntity<ArchiveVo> getNewVersion(String appName, String platform, String arch) {
        return ResponseEntity.ok(amyArchiveService.getNewVersion(appName, platform, arch));
    }

    @GetMapping("/api/version/set-latest-version")
    @Operation(summary = "设置最新版本")
    @PreAuthorize("hasAuthority('archive:set-latest-version')")
    public ResponseEntity<Boolean> setLatestVersion(SetArchiveLatestDto dto) {
        return ResponseEntity.ok(amyArchiveService.setLatest(dto));
    }
}
