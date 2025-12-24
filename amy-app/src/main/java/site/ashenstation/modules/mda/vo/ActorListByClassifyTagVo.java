package site.ashenstation.modules.mda.vo;

import lombok.Data;
import lombok.ToString;
import site.ashenstation.entity.Actor;

import java.util.List;

@Data
@ToString
public class ActorListByClassifyTagVo {
    private Integer id;
    private String title;

    private List<Actor> actors;
}
