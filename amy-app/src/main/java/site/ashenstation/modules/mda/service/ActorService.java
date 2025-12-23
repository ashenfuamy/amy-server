package site.ashenstation.modules.mda.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
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
import site.ashenstation.modules.mda.dto.CreateActorDto;
import site.ashenstation.properties.FileProperties;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorTagMapper actorTagMapper;
    private final ActorMapper actorMapper;
    private final FileProperties fileProperties;
    private final UserResourcePermissionMapper userResourcePermissionMapper;


    @Transactional
    @UpdateResourceCache
    public Boolean createActor(CreateActorDto dto) {
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

        userResourcePermissionMapper.insert( new UserResourcePermission(Integer.valueOf(currentUserId), avatarId));

        return true;
    }


    public void getActorListByClassifyTag() {

    }
}
