package site.ashenstation.modules.security.service;

import com.mybatisflex.core.mask.MaskManager;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ashenstation.entity.Admin;
import site.ashenstation.entity.table.AdminTableDef;
import site.ashenstation.mapper.AdminMapper;
import site.ashenstation.modules.security.dto.JwtUserDto;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminMapper adminMapper;
    private final UserCacheManager userCacheManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUserDto userDto = userCacheManager.getUserCache(username);

        if (userDto == null) {
            Admin admin = MaskManager.execWithoutMask(() -> adminMapper.selectOneByQuery(
                    QueryWrapper.create()
                            .select()
                            .where(AdminTableDef.ADMIN.USERNAME.eq(username))
            ));

            if (admin == null) {
                throw new UsernameNotFoundException(username);
            }

            userDto = new JwtUserDto(admin);
            userCacheManager.addUserCache(username, userDto);
        }
        return userDto;
    }
}
