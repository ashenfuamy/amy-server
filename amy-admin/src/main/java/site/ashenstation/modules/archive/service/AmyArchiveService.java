package site.ashenstation.modules.archive.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.Archive;
import site.ashenstation.entity.table.ArchiveTableDef;
import site.ashenstation.enums.ArchiveStatus;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.ArchiveMapper;
import site.ashenstation.modules.archive.config.properties.ArchiveRepositoryProperties;
import site.ashenstation.modules.archive.dto.PublishArchiveDto;
import site.ashenstation.modules.archive.dto.SetArchiveLatestDto;
import site.ashenstation.modules.archive.vo.ArchiveVo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AmyArchiveService {

    private final ArchiveRepositoryProperties archiveRepositoryProperties;
    private final ArchiveMapper archiveMapper;

    public Boolean publish(PublishArchiveDto dto) {
        File archiveFoot = null;

        if (dto.getName().equals("amy")) {
            archiveFoot = new File(archiveRepositoryProperties.getRoot(), archiveRepositoryProperties.getAmyArchiveDirectory());
        }

        if (archiveFoot == null) {
            throw new BadRequestException("应用不存在");
        }

        Archive exits = archiveMapper.selectOneByCondition(
                ArchiveTableDef.ARCHIVE.NAME.eq(dto.getName())
                        .and(ArchiveTableDef.ARCHIVE.VERSION.eq(dto.getVersion()))
                        .and(ArchiveTableDef.ARCHIVE.PLATFORM.eq(dto.getPlatform()))
                        .and(ArchiveTableDef.ARCHIVE.ARCH.eq(dto.getArch()))
        );

        if (!dto.getReplaceExisting()) {
            if (exits != null) {
                throw new BadRequestException("应用版本已存在");
            }
        }

        File rootDirectory = new File(archiveFoot, "/" + dto.getPlatform() + "/" + dto.getArch() + "/" + dto.getVersion());
        try {


            if (exits == null) {
                Files.createDirectories(Paths.get(rootDirectory.getAbsolutePath()));
            }

            List<MultipartFile> files = dto.getFiles();
            for (MultipartFile f : files) {
                File file = new File(rootDirectory, Objects.requireNonNull(f.getOriginalFilename()));
                f.transferTo(file);
            }

            Archive archivePo = new Archive();
            BeanUtils.copyProperties(dto, archivePo);
            archivePo.setId(IdUtil.fastSimpleUUID());
            archivePo.setDirectoryPath(rootDirectory.getAbsolutePath());
            archivePo.setPublishTime(new Date());
            archivePo.setStatus(ArchiveStatus.PRERELEASE);
            archivePo.setIsLatest(false);


            archiveMapper.insert(archivePo);

            if (dto.getSetToLatest()) {
                SetArchiveLatestDto setArchiveLatestDto = new SetArchiveLatestDto();
                setArchiveLatestDto.setId(archivePo.getId());
                setArchiveLatestDto.setAppName("amy");

                setLatest(setArchiveLatestDto);
            }

            return true;
        } catch (Exception e) {
            rootDirectory.deleteOnExit();
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<ArchiveVo> getVersionList(String appName, String platform, String arch) {
        return QueryChain.of(archiveMapper)
                .select()
                .from(ArchiveTableDef.ARCHIVE)
                .where(ArchiveTableDef.ARCHIVE.NAME.eq(appName))
                .and(ArchiveTableDef.ARCHIVE.PLATFORM.eq(platform))
                .and(ArchiveTableDef.ARCHIVE.ARCH.eq(arch))
                .orderBy(ArchiveTableDef.ARCHIVE.PUBLISH_TIME, true)
                .listAs(ArchiveVo.class);
    }

    public ArchiveVo getNewVersion(String appName, String platform, String arch) {
        Archive archive = archiveMapper.selectOneByCondition(
                ArchiveTableDef.ARCHIVE.NAME.eq(appName)
                        .and(ArchiveTableDef.ARCHIVE.PLATFORM.eq(platform))
                        .and(ArchiveTableDef.ARCHIVE.ARCH.eq(arch))
        );

        ArchiveVo archiveVo = new ArchiveVo();
        BeanUtils.copyProperties(archive, archiveVo);
        return archiveVo;
    }


    public Boolean setLatest(SetArchiveLatestDto dto) {
        Archive archive = new Archive();

        archive.setIsLatest(false);
        archiveMapper.updateByCondition(archive, ArchiveTableDef.ARCHIVE.NAME.eq(dto.getAppName()).and(ArchiveTableDef.ARCHIVE.IS_LATEST.eq(true)));


        Archive targetPo = new Archive();
        targetPo.setIsLatest(true);
        targetPo.setName(dto.getAppName());
        targetPo.setId(dto.getId());
        targetPo.setStatus(ArchiveStatus.RELEASE);

        archiveMapper.update(targetPo);

        return true;
    }

    public String getLatestArchivePath(String platform, String arch, String appName, String archive) {
        Archive latestArchive = archiveMapper.selectOneByCondition(
                ArchiveTableDef.ARCHIVE.NAME.eq(appName)
                        .and(ArchiveTableDef.ARCHIVE.PLATFORM.eq(platform))
                        .and(ArchiveTableDef.ARCHIVE.ARCH.eq(arch))
                        .and(ArchiveTableDef.ARCHIVE.IS_LATEST.eq(true))
        );

        if (latestArchive == null) {
            throw new BadRequestException("版本不存在");
        }

        return archiveRepositoryProperties.getUriPath()
                + "/"
                + latestArchive.getName()
                + "/"
                + latestArchive.getPlatform()
                + "/"
                + latestArchive.getArch()
                + "/"
                + latestArchive.getVersion()
                + "/"
                + archive;
    }
}
