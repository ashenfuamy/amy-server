package site.ashenstation.amyserver.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.config.properties.FileProperties;
import site.ashenstation.amyserver.dto.CreateActorDto;
import site.ashenstation.amyserver.entity.ActorPo;
import site.ashenstation.amyserver.entity.ActorTagPo;
import site.ashenstation.amyserver.entity.MdaTagPo;
import site.ashenstation.amyserver.entity.ResourcePermissionPo;
import site.ashenstation.amyserver.entity.table.ActorPoTableDef;
import site.ashenstation.amyserver.mapper.ActorMapper;
import site.ashenstation.amyserver.mapper.ActorTagMapper;
import site.ashenstation.amyserver.mapper.MdaTagMapper;
import site.ashenstation.amyserver.mapper.ResourcePermissionMapper;
import site.ashenstation.amyserver.utils.SecurityUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorMapper actorMapper;
    private final FileProperties fileProperties;
    private final ResourcePermissionMapper resourcePermissionMapper;
    private final ActorTagMapper actorTagMapper;

    @Transactional
    public Boolean createActor(CreateActorDto actorDto) {
        ActorPo actorPo = actorMapper.selectOneByQuery(QueryWrapper.create()
                .select()
                .where(ActorPoTableDef.ACTOR_PO.NAME.eq(actorDto.getName()))
                .and(ActorPoTableDef.ACTOR_PO.NAME.eq(SecurityUtils.getCurrentUserId()))
        );

        if (actorPo != null) {
            throw new BadRequestException("演员已存在");
        }

        ActorTagPo tag = actorDto.getTag();

        if (tag.getId() == null) {
            actorTagMapper.insert(tag);
        }

        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + fileProperties.getImageExt();
        File avatarDest = new File(fileProperties.getAvatarRoot(), avatarName);

        try {
            actorDto.getAvatarFile().transferTo(avatarDest);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        ActorPo actor = new ActorPo();
        actor.setCreatorId(SecurityUtils.getCurrentUserId());
        actor.setName(actorDto.getName());
        actor.setTag(tag);
        actor.setIntroduction(actorDto.getIntroduction());
        actor.setCreateTime(new Date());
        actor.setAvatarPath(fileProperties.getAvatarResourcePrefix() + "/" + avatarName);
        actor.setAvatarName(avatarName);

        actor.setTagId(tag.getId());

        actorMapper.insert(actor);

        ResourcePermissionPo resourcePermissionPo = new ResourcePermissionPo(SecurityUtils.getCurrentUserId(), avatarId);

        resourcePermissionMapper.insert(resourcePermissionPo);
        return true;
    }


    public List<ActorTagPo> getTags() {
        return actorTagMapper.selectAll();
    }

    public List<ActorPo> getActors() {
        return actorMapper.selectAllWithRelations();
    }
}
