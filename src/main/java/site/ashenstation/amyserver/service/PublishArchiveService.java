package site.ashenstation.amyserver.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ashenstation.amyserver.config.properties.ArchiveRepositoryProperties;

@Service
@RequiredArgsConstructor
public class PublishArchiveService {

    private final ArchiveRepositoryProperties archiveRepositoryProperties;

    @PostConstruct
    private void c() {
    }
}
