package site.ashenstation.modules.mda.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaSummary;
import site.ashenstation.entity.MdaTag;
import site.ashenstation.entity.table.MdaSummaryActorMapTableDef;
import site.ashenstation.entity.table.MdaSummaryTableDef;
import site.ashenstation.enums.MosaicType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.*;
import site.ashenstation.modules.security.vo.ActorSummariesVo;
import site.ashenstation.properties.FileProperties;
import site.ashenstation.utils.SummaryDto;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final MdaPublisherMapper publisherMapper;
    private final MdaSummaryTagMapper detailTagMapper;
    private final MdaTagMapper tagMapper;
    private final MdaSummaryActorMapMapper mdaSummaryActorMapMapper;
    private final FileProperties fileProperties;
    private final MdaSummaryMapper summaryMapper;
    private final MdaSerialMapper serialMapper;
    private final MdaSummaryTagMapper summaryTagMapper;


    public List<MdaPublisher> getPublishers() {
        return publisherMapper.selectAll();
    }

    public List<MdaTag> getSummaryTags() {
        return tagMapper.selectAll();
    }

    public List<ActorSummariesVo> getSummaryTagsByActor(Integer actorId) {
        return QueryChain.of(mdaSummaryActorMapMapper)
                .select(
                        MdaSummaryActorMapTableDef.MDA_SUMMARY_ACTOR_MAP.ACTOR_ID,
                        MdaSummaryActorMapTableDef.MDA_SUMMARY_ACTOR_MAP.SUMMARY_ID,
                        MdaSummaryTableDef.MDA_SUMMARY.ALL_COLUMNS
                )
                .from(MdaSummaryActorMapTableDef.MDA_SUMMARY_ACTOR_MAP)
                .leftJoin(MdaSummaryTableDef.MDA_SUMMARY).on(MdaSummaryTableDef.MDA_SUMMARY.ID.eq(MdaSummaryActorMapTableDef.MDA_SUMMARY_ACTOR_MAP.SUMMARY_ID))
                .where(MdaSummaryActorMapTableDef.MDA_SUMMARY_ACTOR_MAP.ACTOR_ID.eq(actorId))
                .listAs(ActorSummariesVo.class);
    }

    public MdaSummary createSummary(SummaryDto dto) {
        MdaSummary mdaSummary = new MdaSummary();
        BeanUtils.copyProperties(dto, mdaSummary);

        String posterId = IdUtil.fastSimpleUUID();
        String posterName = posterId + fileProperties.getImageExt();

        File posterDest = new File(fileProperties.getPosterRoot(), posterName);

        try {
            dto.getPosterFile().transferTo(posterDest);
        } catch (IOException e) {
            throw new BadRequestException("");
        }

        mdaSummary.setPosterUrl(fileProperties.getPosterResourcePrefix() + "/" + posterName);
        mdaSummary.setMosaicType(MosaicType.find(dto.getMosaicType()));
        mdaSummary.setCreatedAt(new Date());

        MdaPublisher publisher = dto.getPublisher();

        if (publisher.getId() == null) {
            publisherMapper.insert(publisher);
        }

        mdaSummary.setPublisherId(publisher.getId());

        return mdaSummary;
    }

    @UpdateResourceCache
    public void updateResourcePermissionCache() {
    }

}
