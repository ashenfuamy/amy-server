package site.ashenstation.modules.security.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.ashenstation.entity.Admin;
import site.ashenstation.entity.Permission;
import site.ashenstation.entity.table.AdminTableDef;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.mapper.AdminMapper;
import site.ashenstation.mapper.PermissionMapper;
import site.ashenstation.modules.security.dto.AuthenticationDto;
import site.ashenstation.modules.security.dto.JwtUserDto;
import site.ashenstation.modules.security.vo.AdminInfoVo;
import site.ashenstation.modules.security.vo.AuthResVo;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final AdminMapper adminMapper;
    private final RedisUtils redisUtils;
    private final PermissionMapper permissionMapper;

    public AuthResVo loginWithUsernamePassword(AuthenticationDto dto) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, dto.getPassword());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        JwtUserDto jwtUserDto = (JwtUserDto) authenticate.getPrincipal();

        Admin admin = jwtUserDto.getAdmin();
        admin.setPassword(null);

        HashMap<String, String> claims = new HashMap<>();

        claims.put(AmyConstants.JWT_CLAIM_USERNAME, admin.getUsername());
        claims.put(AmyConstants.JWT_CLAIM_USER_ID, admin.getId().toString());
        claims.put(AmyConstants.JWT_CLAIM_TOKEN_TYPE, JwtTokenType.LOGIN.getType());
        claims.put(AmyConstants.JWT_CLAIM_UID, IdUtil.fastUUID());

        String token = tokenProvider.createToken(admin.getUsername(), claims, securityProperties.getTokenValidityInSeconds());

        Admin admin1 = UpdateEntity.of(Admin.class, admin.getId());
        admin1.setLastLogin(new Date());
        adminMapper.update(admin1);

        return new AuthResVo(token, admin);
    }

    public AdminInfoVo getInfo() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        Admin admin = adminMapper.selectOneById(currentUserId);

        AdminInfoVo adminInfoVo = new AdminInfoVo();

        BeanUtils.copyProperties(admin, adminInfoVo);

        return adminInfoVo;
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

            return grantedAuthorities;
        }

        Admin admin
                = adminMapper.selectOneWithRelationsByQuery(QueryWrapper.create().select().where(AdminTableDef.ADMIN.USERNAME.eq(username)));

        List<Permission> permissions = admin.getPermissions();
        permissions.forEach(c -> {
            String p = c.getField() + ":" + c.getCode();
            redisUtils.sSet(key, p);
            grantedAuthorities.add(new SimpleGrantedAuthority(p));
        });

        return grantedAuthorities;
    }
}
