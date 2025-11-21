package site.ashenstation.amyserver.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.config.properties.FileProperties;
import site.ashenstation.amyserver.dto.CreateSeriesDto;
import site.ashenstation.amyserver.entity.*;
import site.ashenstation.amyserver.entity.table.MdaActorSummaryMapPoTableDef;
import site.ashenstation.amyserver.entity.table.MdaSummaryPoTableDef;
import site.ashenstation.amyserver.entity.table.MdaTagPoTableDef;
import site.ashenstation.amyserver.mapper.*;
import site.ashenstation.amyserver.utils.enums.MdaSummaryType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MdaVideoService {

    private final MdaTagMapper tagMapper;
    private final MdaPublisherMapper publisherMapper;
    private final MdaSummaryMapper summaryMapper;
    private final MdaActorSummaryMapMapper actorSummaryMapMapper;
    private final FileProperties fileProperties;
    private final MdaVideoSeriesMapper videoSeriesMapper;
    private final MdaSummaryTagMapMapper summaryTagMapMapper;

    public List<MdaPublisherPo> getMdaPublishers() {
        return publisherMapper.selectAll();
    }

    public List<MdaTagPo> getMdaTags() {
        return tagMapper.selectListByQuery(QueryWrapper.create()
                .select(MdaTagPoTableDef.MDA_TAG_PO.ID, MdaTagPoTableDef.MDA_TAG_PO.TITLE)
                .where(MdaTagPoTableDef.MDA_TAG_PO.ENABLED.eq(true))
        );
    }

    @Transactional
    public void createMdaSeries(CreateSeriesDto seriesDto) {
        MdaSummaryPo mdaSummaryPo = summaryMapper.selectOneByCondition(MdaSummaryPoTableDef.MDA_SUMMARY_PO.TITLE.eq(seriesDto.getTitle()));

        if (mdaSummaryPo != null) {
            MdaActorSummaryMapPo mdaActorSummaryMapPo
                    = actorSummaryMapMapper.selectOneByCondition(MdaActorSummaryMapPoTableDef.MDA_ACTOR_SUMMARY_MAP_PO.SUMMARY_ID.eq(mdaSummaryPo.getId()));

            if (mdaActorSummaryMapPo != null) {
                throw new BadRequestException("合集已存在");
            }
        }

        MultipartFile posterFile = seriesDto.getPosterFile();

        String posterId = IdUtil.fastSimpleUUID();
        String posterName = posterId + fileProperties.getImageExt();

        File posterDest = new File(fileProperties.getPosterRoot(), posterName);

        try {
            posterFile.transferTo(posterDest);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        if (seriesDto.getPublisher().getId() == null) {
            publisherMapper.insert(seriesDto.getPublisher());
        }

        seriesDto.getTags().forEach(mdaTagPo -> {
            if (mdaTagPo.getId() == null) {
                mdaTagPo.setEnabled(true);
                tagMapper.insert(mdaTagPo);
            }
        });


        String posterRelativePath = fileProperties.getPosterResourcePrefix() + "/" + posterName;

        MdaSummaryPo msPo = new MdaSummaryPo();
        msPo.setTitle(seriesDto.getTitle());
        msPo.setSerialNumber(seriesDto.getSerialNumber());
        msPo.setPosterUrl(posterRelativePath);
        msPo.setPosterName(posterName);
        msPo.setMosaicType(seriesDto.getMosaicType());
        msPo.setType(MdaSummaryType.SERIES);

        summaryMapper.insert(msPo);

        MdaVideoSeriesPo mdaVideoSeriesPo = new MdaVideoSeriesPo();
        mdaVideoSeriesPo.setPublisherId(seriesDto.getPublisher().getId());
        mdaVideoSeriesPo.setSummaryId(msPo.getId());

        videoSeriesMapper.insert(mdaVideoSeriesPo);

        ArrayList<MdaSummaryTagMapPo> mdaSummaryTagMapPos = new ArrayList<>();

        seriesDto.getTags().forEach(mdaTagPo -> {
            MdaSummaryTagMapPo mdaSummaryTagMapPo = new MdaSummaryTagMapPo();
            mdaSummaryTagMapPo.setTagId(mdaTagPo.getId());
            mdaSummaryTagMapPo.setSummaryId(msPo.getId());
            mdaSummaryTagMapPos.add(mdaSummaryTagMapPo);
        });

        summaryTagMapMapper.insertBatch(mdaSummaryTagMapPos);

    }
}
