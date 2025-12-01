package site.ashenstation.amyserver.service;

import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.config.properties.ArchiveRepositoryProperties;
import site.ashenstation.amyserver.dto.PublishArchiveDto;
import site.ashenstation.amyserver.dto.SetArchiveLatestDto;
import site.ashenstation.amyserver.entity.table.ArchivePoTableDef;
import site.ashenstation.amyserver.mapper.ArchiveMapper;
import site.ashenstation.amyserver.entity.ArchivePo;
import site.ashenstation.amyserver.utils.enums.ArchiveStatus;
import site.ashenstation.amyserver.vo.ArchiveVo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepositoryProperties archiveRepositoryProperties;
    private final ArchiveMapper archiveMapper;

    @Transactional
    public Boolean publish(PublishArchiveDto dto) {

        File archiveFoot = null;

        if (dto.getName().equals("amy")) {
            archiveFoot = new File(archiveRepositoryProperties.getRoot(), archiveRepositoryProperties.getAmyArchiveDirectory());
        }

        if (archiveFoot == null) {
            throw new BadRequestException("应用不存在");
        }

        ArchivePo exits = archiveMapper.selectOneByCondition(
                ArchivePoTableDef.ARCHIVE_PO.NAME.eq(dto.getName())
                        .and(ArchivePoTableDef.ARCHIVE_PO.VERSION.eq(dto.getVersion()))
                        .and(ArchivePoTableDef.ARCHIVE_PO.PLATFORM.eq(dto.getPlatform()))
                        .and(ArchivePoTableDef.ARCHIVE_PO.ARCH.eq(dto.getArch()))
        );

        if (!dto.getReplaceExisting()) {
            if (exits != null) {
                throw new BadRequestException("应用版本已存在");
            }
        }

        File rootDirectory = new File(archiveFoot, "/"  +dto.getPlatform() + "/" + dto.getArch() + "/" + dto.getVersion());
        try {


            if (exits == null) {
                Files.createDirectories(Paths.get(rootDirectory.getAbsolutePath()));
            }

            List<MultipartFile> files = dto.getFiles();
            for (MultipartFile f : files) {
                File file = new File(rootDirectory, Objects.requireNonNull(f.getOriginalFilename()));
                f.transferTo(file);
            }

            ArchivePo archivePo = new ArchivePo();
            BeanUtils.copyProperties(dto, archivePo);
            archivePo.setDirectoryPath(rootDirectory.getAbsolutePath());
            archivePo.setPublishTime(new Date());
            archivePo.setStatus(ArchiveStatus.PRERELEASE);
            archivePo.setIsLatest(false);


            archiveMapper.insert(archivePo);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            rootDirectory.deleteOnExit();
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<ArchiveVo> getVersionList(String appName) {
        return QueryChain.of(archiveMapper)
                .select()
                .from(ArchivePoTableDef.ARCHIVE_PO)
                .where(ArchivePoTableDef.ARCHIVE_PO.NAME.eq(appName))
                .orderBy(ArchivePoTableDef.ARCHIVE_PO.PUBLISH_TIME, true)
                .listAs(ArchiveVo.class);
    }

    @Transactional
    public Boolean setLatest(SetArchiveLatestDto dto) {

        ArchivePo archivePo = new ArchivePo();
        archivePo.setIsLatest(false);

        archiveMapper.updateByCondition(archivePo, ArchivePoTableDef.ARCHIVE_PO.NAME.eq(dto.getAppName()).and(ArchivePoTableDef.ARCHIVE_PO.IS_LATEST.ge(true)));

        ArchivePo targetPo = new ArchivePo();
        targetPo.setIsLatest(true);
        targetPo.setName(dto.getAppName());
        targetPo.setId(dto.getId());
        targetPo.setStatus(ArchiveStatus.RELEASE);

        archiveMapper.update(targetPo);

        return true;
    }


    public String getLatestArchivePath(String platform, String arch, String appName, String archive) {
        ArchivePo archivePo = archiveMapper.selectOneByCondition(
                ArchivePoTableDef.ARCHIVE_PO.NAME.eq(appName)
                        .and(ArchivePoTableDef.ARCHIVE_PO.IS_LATEST.eq(true))
                        .and(ArchivePoTableDef.ARCHIVE_PO.PLATFORM.eq(platform))
                        .and(ArchivePoTableDef.ARCHIVE_PO.ARCH.eq(arch))
        );

        if (archivePo == null) {
            throw new BadRequestException("版本不存在");
        }

        return archiveRepositoryProperties.getAmyArchiveDirectory()
                + "/"
                + archivePo.getName()
                 + "/"
                + archivePo.getPlatform()
                + "/"
                + archivePo.getArch()
                + "/"
                + archivePo.getVersion()
                + "/"
                + archive;
    }
}
