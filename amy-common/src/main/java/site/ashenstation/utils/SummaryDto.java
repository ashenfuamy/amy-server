package site.ashenstation.utils;

import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaTag;

import java.util.List;

public interface SummaryDto {

    String getTitle();

    String getSubtitle();

    String getSerialNumber();

    Integer getMosaicType();

    MdaPublisher getPublisher();

    MultipartFile getPosterFile();

    List<Integer> getActors();

    List<MdaTag> getTags();
}
