package site.ashenstation.modules.mda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.entity.MdaSummary;
import site.ashenstation.entity.MdaVideo;
import site.ashenstation.enums.MdaSummaryType;
import site.ashenstation.mapper.MdaSummaryMapper;
import site.ashenstation.modules.mda.dto.CreateVideoDto;
import site.ashenstation.modules.mda.vo.VideoCreatedVo;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final MdaSummaryMapper summaryMapper;

    @Transactional
    public VideoCreatedVo createVideo(CreateVideoDto dto) {

        MdaSummary mdaSummary = new MdaSummary();
        mdaSummary.setType(MdaSummaryType.VIDEO);
        mdaSummary.setCreatedAt(new Date());

        BeanUtils.copyProperties(dto, mdaSummary);

        if (dto.getSerialId() != null) {

            summaryMapper.insert(mdaSummary);

            MdaVideo mdaVideo = new MdaVideo();
            mdaVideo.setSize(dto.getSize());
            mdaVideo.setConverted(false);
            mdaVideo.setSummaryId(mdaSummary.getId());
            mdaVideo.setSeriesId(dto.getSerialId());
            
        }


        return new VideoCreatedVo();
    }
}
