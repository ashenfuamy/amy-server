package site.ashenstation.modules.appuser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.entity.User;
import site.ashenstation.entity.table.UserTableDef;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.UserMapper;
import site.ashenstation.modules.appuser.dto.CreateUserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

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


        return 1;
    }
}
