package site.ashenstation.amyserver.service;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ashenstation.amyserver.dto.JwtUserDto;
import site.ashenstation.amyserver.entity.RolePo;
import site.ashenstation.amyserver.entity.UserPo;
import site.ashenstation.amyserver.entity.table.UserPoTableDef;
import site.ashenstation.amyserver.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserCacheManager userCacheManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        JwtUserDto userDto = userCacheManager.getUserCache(username);

        if (userDto == null) {
            UserPo user = userMapper.selectOneByQuery(
                    QueryWrapper.create()
                            .select()
                            .where(UserPoTableDef.USER_PO.USERNAME.eq(username))
            );


            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            userDto = new JwtUserDto(user);

            userCacheManager.addUserCache(username, userDto);
        }


        return userDto;
    }
}
