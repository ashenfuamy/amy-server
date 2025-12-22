package site.ashenstation.modules.security.service;

import com.mybatisflex.core.mask.MaskManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ashenstation.entity.User;
import site.ashenstation.entity.table.UserTableDef;
import site.ashenstation.mapper.UserMapper;
import site.ashenstation.modules.security.dto.JwtUserDto;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserCacheManager userCacheManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUserDto userDto = userCacheManager.getUserCache(username);

        if (userDto == null) {
            User user = MaskManager.execWithoutMask(() -> userMapper.selectOneByCondition(
                    UserTableDef.USER.USERNAME.eq(username)
            ));

            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            userDto = new JwtUserDto(user);
            userCacheManager.addUserCache(username, userDto);
        }
        return userDto;
    }
}
