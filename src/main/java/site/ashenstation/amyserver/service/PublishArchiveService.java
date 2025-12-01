package site.ashenstation.amyserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.config.properties.ArchiveRepositoryProperties;
import site.ashenstation.amyserver.dto.PublishArchiveDto;
import site.ashenstation.amyserver.entity.ArchivePo;
import site.ashenstation.amyserver.mapper.ArchiveMapper;
import site.ashenstation.amyserver.utils.enums.ArchiveStatus;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PublishArchiveService {

    private final ArchiveRepositoryProperties archiveRepositoryProperties;
    private final ArchiveMapper archiveMapper;

    public Boolean publish(PublishArchiveDto dto) {
        System.out.println(dto);
        File archiveFoot = null;

        if (dto.getName().equals("amy")) {
            archiveFoot = new File(archiveRepositoryProperties.getRoot(), archiveRepositoryProperties.getAmyArchiveDirectory());
        }

        if (archiveFoot == null) {
            throw new BadRequestException("应用不存在");
        }

        File versionRoot = new File(archiveFoot, dto.getVersion());
        if (!dto.getReplaceExisting()) {
            if (versionRoot.exists()) {
                throw new BadRequestException("应用版本已存在");
            }
        }

        try {
            versionRoot.mkdir();

            List<MultipartFile> files = dto.getFiles();
            for (MultipartFile f : files) {
                File file = new File(versionRoot, Objects.requireNonNull(f.getOriginalFilename()));
                f.transferTo(file);
            }

            ArchivePo archivePo = new ArchivePo();

            archivePo.setName(dto.getName());
            archivePo.setVersion(dto.getVersion());
            archivePo.setDirectoryPath(versionRoot.getAbsolutePath());
            archivePo.setPublishTime(new Date());
            archivePo.setStatus(ArchiveStatus.PRERELEASE);
            archivePo.setIsLatest(false);

            archiveMapper.insert(archivePo);


            return true;

        } catch (Exception e) {
            e.printStackTrace();
            versionRoot.deleteOnExit();
            throw new BadRequestException(e.getMessage());
        }
    }
}
