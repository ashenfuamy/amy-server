package site.ashenstation.modules.mda.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.entity.*;
import site.ashenstation.entity.table.MdaSummaryTableDef;
import site.ashenstation.enums.MdaSummaryType;
import site.ashenstation.enums.MdaTagType;
import site.ashenstation.enums.MosaicType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.*;
import site.ashenstation.modules.mda.dto.CreateSerialDto;
import site.ashenstation.modules.mda.vo.MdaSerialVo;
import site.ashenstation.properties.FileProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialService {

    private final FileProperties fileProperties;
    private final MdaPublisherMapper publisherMapper;
    private final MdaSerialMapper serialMapper;
    private final MdaSummaryMapper summaryMapper;
    private final MdaSummaryActorMapMapper mdaSummaryActorMapMapper;
    private final MdaTagMapper tagMapper;
    private final MdaSummaryTagMapper summaryTagMapper;


    @UpdateResourceCache
    @Transactional
    public Boolean createSerial(CreateSerialDto dto) {
        List<Integer> actors = dto.getActors();

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
        mdaSummary.setType(MdaSummaryType.SERIAL);
        mdaSummary.setMosaicType(MosaicType.find(dto.getMosaicType()));
        mdaSummary.setCreatedAt(new Date());

        MdaPublisher publisher = dto.getPublisher();

        if (publisher.getId() == null) {
            publisherMapper.insert(publisher);
        }

        mdaSummary.setPublisherId(publisher.getId());

        summaryMapper.insert(mdaSummary);

        ArrayList<MdaSummaryTag> mdaSummaryTags = new ArrayList<>();
        List<MdaTag> tags = dto.getTags();

        tags.forEach(t -> {
            if (t.getId() == null) {
                t.setType(MdaTagType.ADULT);
                tagMapper.insert(t);
            }
            mdaSummaryTags.add(new MdaSummaryTag(mdaSummary.getId(), t.getId()));
        });

        summaryTagMapper.insertBatch(mdaSummaryTags);

        MdaSerial mdaSerial = new MdaSerial();
        mdaSerial.setSummaryId(mdaSummary.getId());

        serialMapper.insert(mdaSerial);
        ArrayList<MdaSummaryActorMap> mdaSummaryActorMaps = new ArrayList<>();

        actors.forEach(ac ->{
            mdaSummaryActorMaps.add(new MdaSummaryActorMap(ac, mdaSummary.getId()));
        });

        mdaSummaryActorMapMapper.insertBatch(mdaSummaryActorMaps);

        return true;
    }


    public List<MdaSerialVo> getSerialList() {
        return summaryMapper.selectListByQueryAs(
                QueryWrapper.create()
                        .select(
                                MdaSummaryTableDef.MDA_SUMMARY.ID,
                                MdaSummaryTableDef.MDA_SUMMARY.TITLE
                        )
                        .where(MdaSummaryTableDef.MDA_SUMMARY.TYPE.eq(MdaSummaryType.SERIAL))
                ,
                MdaSerialVo.class
        );
    }
}
