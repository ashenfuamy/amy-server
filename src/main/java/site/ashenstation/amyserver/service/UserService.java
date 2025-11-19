package site.ashenstation.amyserver.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.config.properties.FileProperties;
import site.ashenstation.amyserver.config.properties.SecurityProperties;
import site.ashenstation.amyserver.dto.RegisterUerDto;
import site.ashenstation.amyserver.dto.SetUserRolesDto;
import site.ashenstation.amyserver.dto.UpdateUserDto;
import site.ashenstation.amyserver.entity.UserPo;
import site.ashenstation.amyserver.entity.UserRolePo;
import site.ashenstation.amyserver.entity.table.UserPoTableDef;
import site.ashenstation.amyserver.entity.table.UserRolePoTableDef;
import site.ashenstation.amyserver.mapper.UserMapper;
import site.ashenstation.amyserver.mapper.UserRoleMapper;
import site.ashenstation.amyserver.utils.RedisUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRolePoMapper;
    private final RedisUtils redisUtils;
    private final SecurityProperties securityProperties;
    private final FileProperties fileProperties;

    public Boolean register(RegisterUerDto registerUerDto) {

        UserPo exist = userMapper.selectOneByQuery(QueryWrapper.create()
                .select(UserPoTableDef.USER_PO.ID)
                .where(UserPoTableDef.USER_PO.USERNAME.eq(registerUerDto.getUsername())));

        if (exist != null) {
            throw new BadRequestException("用户名已存在！");
        }

        if (registerUerDto.getEmail() != null) {
            UserPo emilExist = userMapper.selectOneByQuery(QueryWrapper.create().select(UserPoTableDef.USER_PO.ID).where(UserPoTableDef.USER_PO.EMAIL.eq(registerUerDto.getEmail())));
            if (emilExist != null) {
                throw new BadRequestException("邮箱已被使用！");
            }
        }


        if (registerUerDto.getPhoneNumber() != null) {
            UserPo phoneExist = userMapper.selectOneByQuery(QueryWrapper.create().select(UserPoTableDef.USER_PO.ID).where(UserPoTableDef.USER_PO.PHONE_NUMBER.eq(registerUerDto.getPhoneNumber())));
            if (phoneExist != null) {
                throw new BadRequestException("邮箱已被使用！");
            }
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        UserPo userPo = new UserPo();
        userPo.setLocked(false);
        userPo.setEnabled(false);

        BeanUtils.copyProperties(registerUerDto, userPo);

        userPo.setPassword(passwordEncoder.encode(registerUerDto.getPassword()));

        int insert = userMapper.insert(userPo);

        return insert == 1;
    }

    public void recordLoginDataTime(String currentUserId) {

        UserPo userPo = UpdateEntity.of(UserPo.class, currentUserId);

        userPo.setLastLogin(new Date());
        userMapper.update(userPo);
    }

    public List<GrantedAuthority> getPermissions(String username) {
        String key = securityProperties.getResourcePermissionKey() + username;
        Set<Object> members = redisUtils.members(key);
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (members != null && !members.isEmpty()) {
            for (Object obj : members) {
                if (obj instanceof String) {
                    grantedAuthorities.add(new SimpleGrantedAuthority((String) obj));
                }
            }
        } else {
            UserPo userPo = userMapper.selectOneWithRelationsByQuery(QueryWrapper.create()
                    .select(UserPoTableDef.USER_PO.ID)
                    .where(UserPoTableDef.USER_PO.USERNAME.eq(username))
            );

            userPo.getRole().forEach(r -> {
                redisUtils.sSet(key, "ROLE_" + r.getCode());
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + r.getCode()));
            });

        }

        return grantedAuthorities;
    }

    @Transactional
    public Boolean setUserRoles(SetUserRolesDto dto) {
        String userId = dto.getUserId();

        userRolePoMapper.deleteByCondition(UserRolePoTableDef.USER_ROLE_PO.USER_ID.eq(userId));

        ArrayList<UserRolePo> userRolePos = new ArrayList<>();

        dto.getRoleId().forEach(c -> {
            UserRolePo userRolePo = new UserRolePo();
            userRolePo.setRoleId(c);
            userRolePo.setUserId(dto.getUserId());
            userRolePos.add(userRolePo);
        });


        userRolePoMapper.insertBatch(userRolePos);

        return true;
    }

    public Boolean setUserInfo(UpdateUserDto userInfo) {

        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(userInfo, userPo);

        MultipartFile avatarFile = userInfo.getAvatarFile();

        if (avatarFile != null) {
            String avatarId = IdUtil.fastSimpleUUID();
            String avatarName = avatarId + fileProperties.getImageExt();
            File avatarDest = new File(fileProperties.getAvatarRoot(), avatarName);

            try {
                avatarFile.transferTo(avatarDest);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }

            userPo.setAvatarPath(fileProperties.getAvatarResourcePrefix() + "/" + avatarName);
        }

        userMapper.update(userPo, true);

        return true;
    }


}
