package site.ashenstation.amyserver.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.dto.RegisterUerDto;
import site.ashenstation.amyserver.dto.SetUserRolesDto;
import site.ashenstation.amyserver.entity.UserPo;
import site.ashenstation.amyserver.entity.UserRolePo;
import site.ashenstation.amyserver.entity.table.UserPoTableDef;
import site.ashenstation.amyserver.entity.table.UserRolePoTableDef;
import site.ashenstation.amyserver.mapper.UserMapper;
import site.ashenstation.amyserver.mapper.UserRoleMapper;
import site.ashenstation.amyserver.utils.RedisUtils;

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
        Set<Object> members = redisUtils.members(username);

        return new ArrayList<>();
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
}
