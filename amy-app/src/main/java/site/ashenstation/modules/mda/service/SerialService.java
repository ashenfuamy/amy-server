package site.ashenstation.modules.mda.service;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.entity.*;
import site.ashenstation.entity.table.MdaSummaryTableDef;
import site.ashenstation.enums.MdaSummaryType;
import site.ashenstation.enums.MdaTagType;
import site.ashenstation.mapper.*;
import site.ashenstation.modules.mda.dto.CreateSerialDto;
import site.ashenstation.modules.mda.vo.MdaSerialVo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialService {

    private final MdaSerialMapper serialMapper;
    private final MdaSummaryMapper summaryMapper;
    private final MdaSummaryActorMapMapper mdaSummaryActorMapMapper;
    private final MdaTagMapper tagMapper;
    private final MdaSummaryTagMapper summaryTagMapper;
    private final SummaryService summaryService;


    @UpdateResourceCache
    @Transactional
    public Boolean createSerial(CreateSerialDto dto) {
        MdaSummary mdaSummary = summaryService.createSummary(dto);
        mdaSummary.setType(MdaSummaryType.SERIAL);

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
        List<Integer> actors = dto.getActors();
        actors.forEach(ac -> {
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
