package site.ashenstation.modules.security.vo;

import lombok.Data;
import lombok.ToString;
import site.ashenstation.entity.MdaSummary;

import java.util.List;

@Data
@ToString
public class ActorSummariesVo {
    private Integer actorId;
    private List<MdaSummary> actorSummaries;
}
