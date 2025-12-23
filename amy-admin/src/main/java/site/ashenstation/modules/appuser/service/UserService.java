package site.ashenstation.modules.appuser.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.User;
import site.ashenstation.entity.table.UserTableDef;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.UserMapper;
import site.ashenstation.modules.appuser.dto.CreateUserDto;
import site.ashenstation.properties.FileProperties;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final FileProperties fileProperties;

    @Transactional
    public Integer createUser(CreateUserDto dto) {
        User usernameExist = userMapper.selectOneByCondition(
                UserTableDef.USER.USERNAME.eq(dto.getUsername())
        );
        if (usernameExist != null) {
            throw new BadRequestException("用户名已存在");
        }

        User phoneExist = userMapper.selectOneByCondition(
                UserTableDef.USER.PHONE_NUMBER.eq(dto.getPhone())
        );
        if (phoneExist != null) {
            throw new BadRequestException("电话号已被使用");
        }

        User emExist = userMapper.selectOneByCondition(
                UserTableDef.USER.EMAIL.eq(dto.getEmail())
        );
        if (emExist != null) {
            throw new BadRequestException("邮箱已被使用");
        }

        User user = new User();

        BeanUtils.copyProperties(dto, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setLastLogin(new Date());
        user.setLocked(false);

        String encodePassword = new BCryptPasswordEncoder(12).encode(dto.getPassword());
        user.setPassword(encodePassword);
        
        MultipartFile avatar = dto.getAvatar();
        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + fileProperties.getImageExt();
        File avatarDest = new File(fileProperties.getAvatarRoot(), avatarName);

        try {
            avatar.transferTo(avatarDest);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        user.setAvatarPath(fileProperties.getAvatarResourcePrefix() + "/" + avatarName);

        return userMapper.insertSelective(user);
    }
}
