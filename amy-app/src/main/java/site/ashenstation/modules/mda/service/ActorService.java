package site.ashenstation.modules.mda.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.entity.Actor;
import site.ashenstation.entity.ActorTag;
import site.ashenstation.entity.UserResourcePermission;
import site.ashenstation.entity.table.ActorTableDef;
import site.ashenstation.entity.table.ActorTagTableDef;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.ActorMapper;
import site.ashenstation.mapper.ActorTagMapper;
import site.ashenstation.mapper.UserResourcePermissionMapper;
import site.ashenstation.modules.mda.dto.ActorDto;
import site.ashenstation.modules.mda.vo.ActorListByClassifyTagVo;
import site.ashenstation.properties.FileProperties;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorTagMapper actorTagMapper;
    private final ActorMapper actorMapper;
    private final FileProperties fileProperties;
    private final UserResourcePermissionMapper userResourcePermissionMapper;


    @Transactional
    @UpdateResourceCache
    public Boolean createActor(ActorDto dto) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        Actor exist = actorMapper.selectOneByCondition(
                ActorTableDef.ACTOR.CREATOR_ID.eq(currentUserId)
                        .and(ActorTableDef.ACTOR.NAME.eq(dto.getName()))
        );

        if (exist != null) {
            throw new BadRequestException("已存在");
        }

        ActorTag actorTag = dto.getActorTag();

        if (actorTag.getId() == null) {
            actorTagMapper.insert(actorTag);
        }

        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + fileProperties.getImageExt();
        File avatarDest = new File(fileProperties.getAvatarRoot(), avatarName);

        try {
            dto.getAvatarFile().transferTo(avatarDest);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        Actor actor = new Actor();
        BeanUtils.copyProperties(dto, actor);

        actor.setCreatorId(currentUserId);
        actor.setCreateTime(new Date());
        actor.setTagId(actorTag.getId());
        actor.setAvatarPath(fileProperties.getAvatarResourcePrefix() + "/" + avatarName);

        actorMapper.insert(actor);

        userResourcePermissionMapper.insert(new UserResourcePermission(Integer.valueOf(currentUserId), avatarId));

        return true;
    }

    @Transactional
    public Boolean updateActor(ActorDto dto) {
        Integer id = dto.getId();
        if (id == null) {
            throw new BadRequestException("ID 不存在");
        }

        Actor actor = actorMapper.selectOneById(id);

        actor.setName(dto.getName());
        actor.setCountry(dto.getCountry());
        actor.setIntroduction(dto.getIntroduction());
        actor.setWebsite(dto.getWebsite());

        if (dto.getActorTag().getId() == null) {
            actorTagMapper.insert(dto.getActorTag());
        }
        actor.setTagId(dto.getActorTag().getId());

        actorMapper.update(actor);

        return true;
    }


    @Transactional
    public Boolean updateActorAvatar(ActorDto dto) {
        Integer id = dto.getId();
        if (id == null) {
            throw new BadRequestException("ID 不存在");
        }

        Actor actor = actorMapper.selectOneById(id);

        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + fileProperties.getImageExt();
        File avatarDest = new File(fileProperties.getAvatarRoot(), avatarName);

        try {
            dto.getAvatarFile().transferTo(avatarDest);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        actor.setAvatarPath(fileProperties.getAvatarResourcePrefix() + "/" + avatarName);

        return true;
    }

    public Integer deleteActor(Integer id) {
        return actorMapper.deleteById(id);
    }

    public List<ActorTag> getActorTags() {
        return actorTagMapper.selectAll();
    }


    public List<ActorListByClassifyTagVo> getActorListByClassifyTag() {

        String currentUserId = SecurityUtils.getCurrentUserId();

        return QueryChain.of(actorTagMapper)
                .select(
                        ActorTagTableDef.ACTOR_TAG.ID,
                        ActorTagTableDef.ACTOR_TAG.TITLE,
                        ActorTableDef.ACTOR.ID,
                        ActorTableDef.ACTOR.NAME,
                        ActorTableDef.ACTOR.TAG_ID,
                        ActorTableDef.ACTOR.INTRODUCTION,
                        ActorTableDef.ACTOR.AVATAR_PATH,
                        ActorTableDef.ACTOR.CREATOR_ID
                )
                .from(ActorTagTableDef.ACTOR_TAG)
                .leftJoin(ActorTableDef.ACTOR).on(ActorTagTableDef.ACTOR_TAG.ID.eq(ActorTableDef.ACTOR.TAG_ID))
                .where(ActorTableDef.ACTOR.CREATOR_ID.eq(currentUserId))
                .listAs(ActorListByClassifyTagVo.class);
    }

    public List<Actor> getActors() {
        return actorMapper.selectAll();
    }
}
