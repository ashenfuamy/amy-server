package site.ashenstation.modules.security.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.entity.Admin;
import site.ashenstation.entity.AdminPermission;
import site.ashenstation.entity.CustomToken;
import site.ashenstation.entity.Permission;
import site.ashenstation.entity.table.AdminPermissionTableDef;
import site.ashenstation.entity.table.PermissionTableDef;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.AdminMapper;
import site.ashenstation.mapper.AdminPermissionMapper;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.mapper.PermissionMapper;
import site.ashenstation.modules.security.dto.*;
import site.ashenstation.modules.security.vo.AdminInfoVo;
import site.ashenstation.modules.security.vo.AuthResVo;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RsaUtils;
import site.ashenstation.utils.SecurityUtils;
import site.ashenstation.utils.TokenProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final AdminMapper adminMapper;
    private final PermissionMapper permissionMapper;
    private final AdminPermissionMapper adminPermissionMapper;
    private final CustomTokenMapper customTokenMapper;

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

    @Transactional(rollbackFor = Exception.class)
    public Integer addPermission(PermissionDto dto) {
        Permission permission = permissionMapper.selectOneByQuery(
                QueryWrapper.create()
                        .select(PermissionTableDef.PERMISSION.ID)
                        .where(PermissionTableDef.PERMISSION.CODE.eq(dto.getCode()))
                        .and(PermissionTableDef.PERMISSION.FIELD.eq(dto.getField()))
        );

        if (permission != null) {
            throw new BadRequestException("权限已存在");
        }

        Permission permission1 = new Permission();
        BeanUtils.copyProperties(dto, permission1);

        int insert = permissionMapper.insert(permission1);

        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setPermissionId(permission1.getId());
        adminPermission.setAdminId(1);

        adminPermissionMapper.insert(adminPermission);

        return insert;
    }

    @Transactional
    public Integer setPermission(SetPermissionDto dto) {
        Integer adminId = dto.getAdminId();

        adminPermissionMapper.deleteByCondition(AdminPermissionTableDef.ADMIN_PERMISSION.ADMIN_ID.eq(adminId));

        ArrayList<AdminPermission> adminPermissions = new ArrayList<>();
        for (Integer permission : dto.getPermissions()) {
            adminPermissions.add(new AdminPermission(permission, adminId));
        }

        return adminPermissionMapper.insertBatch(adminPermissions);

    }

    @Transactional
    public String generateToken(GenerateTokenDto dto) {
        Integer expire = dto.getExpire();

        List<Integer> permissions = dto.getPermissions();
        ArrayList<String> list = new ArrayList<>();

        permissions.forEach(c -> {
            Permission permission = permissionMapper.selectOneById(c);

            if (permission == null) {
                throw new BadRequestException("权限不存在");
            }

            list.add(permission.getField() + ":" + permission.getCode());
        });

        String permission = String.join(",", list);

        HashMap<String, String> claims = new HashMap<>();

        String uid = IdUtil.fastUUID();

        claims.put(AmyConstants.JWT_CLAIM_PERMISSION, permission);
        claims.put(AmyConstants.JWT_CLAIM_TOKEN_TYPE, JwtTokenType.Authorization.getType());
        claims.put(AmyConstants.JWT_CLAIM_UID, uid);

        String token = tokenProvider.createToken("anonymous", claims, (long) (expire * 60 * 1000));

        CustomToken customToken = new CustomToken();
        customToken.setToken(token);
        customToken.setCreateTime(new Date());
        customToken.setTitle(dto.getTitle());
        customToken.setCreator(Integer.valueOf(SecurityUtils.getCurrentUserId()));
        customToken.setUid(uid);

        customTokenMapper.insert(customToken);

        return token;
    }


}
