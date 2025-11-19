package site.ashenstation.amyserver.service;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.dto.CreatePermissionDto;
import site.ashenstation.amyserver.dto.RoleDto;
import site.ashenstation.amyserver.dto.SetRolePermissionDto;
import site.ashenstation.amyserver.entity.PermissionPo;
import site.ashenstation.amyserver.entity.RolePermissionPo;
import site.ashenstation.amyserver.entity.RolePo;
import site.ashenstation.amyserver.entity.table.PermissionPoTableDef;
import site.ashenstation.amyserver.entity.table.RolePermissionPoTableDef;
import site.ashenstation.amyserver.entity.table.RolePoTableDef;
import site.ashenstation.amyserver.mapper.PermissionMapper;
import site.ashenstation.amyserver.mapper.RoleMapper;
import site.ashenstation.amyserver.mapper.RolePermissionMapper;
import site.ashenstation.amyserver.vo.PermissionVo;
import site.ashenstation.amyserver.vo.RoleVo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    public Boolean addRole(RoleDto roleDto) {

        RolePo rolePo = roleMapper.selectOneByQuery(QueryWrapper.create().select(RolePoTableDef.ROLE_PO.ID).where(RolePoTableDef.ROLE_PO.NAME.eq(roleDto.getName())));

        if (rolePo != null) {
            throw new BadRequestException("角色已存在");
        }

        RolePo po = new RolePo();

        BeanUtils.copyProperties(roleDto, po);
        po.setCode(po.getName().toUpperCase());

        int insert = roleMapper.insert(po);

        return 1 == insert;
    }


    public List<RoleVo> getAllRole() {
        return QueryChain.of(roleMapper)
                .select()
                .listAs(RoleVo.class);
    }


    @Transactional
    public Boolean createPermission(CreatePermissionDto dto) {
        PermissionPo ex1 = permissionMapper.selectOneByQuery(QueryWrapper.create()
                .select(PermissionPoTableDef.PERMISSION_PO.ID)
                .where(PermissionPoTableDef.PERMISSION_PO.NAME.eq(dto.getName()))
                .and(PermissionPoTableDef.PERMISSION_PO.FIELD.eq(dto.getField()))
        );

        if (ex1 != null) throw new BadRequestException("权限已存在");

        PermissionPo ex2 = permissionMapper.selectOneByQuery(QueryWrapper.create()
                .select(PermissionPoTableDef.PERMISSION_PO.ID)
                .where(PermissionPoTableDef.PERMISSION_PO.CODE.eq(dto.getCode()))
        );

        if (ex2 != null) throw new BadRequestException("权限编码已存在");

        PermissionPo permissionPo = new PermissionPo();
        BeanUtils.copyProperties(dto, permissionPo);

        permissionMapper.insert(permissionPo);

        RolePo rolePo = roleMapper.selectOneByQuery(QueryWrapper.create().select(RolePoTableDef.ROLE_PO.ID).where(RolePoTableDef.ROLE_PO.CODE.eq("ADMIN")));

        RolePermissionPo rolePermissionPo = new RolePermissionPo();
        rolePermissionPo.setPermissionId(permissionPo.getId());
        rolePermissionPo.setRoleId(rolePo.getId());

        rolePermissionMapper.insert(rolePermissionPo);
        return true;
    }

    public List<PermissionVo> getAllPermissions() {
        return QueryChain.of(permissionMapper).select().listAs(PermissionVo.class);
    }

    @Transactional
    public Integer setRolePermissions(SetRolePermissionDto dto) {
        RolePo rolePo = roleMapper.selectOneById(dto.getRoleId());
        if (rolePo == null) {
            throw new BadRequestException("角色不存在");
        }

        if(rolePo.getCode().equals("ADMIN")) {
            throw new BadRequestException("操作无效");
        }

        rolePermissionMapper.deleteByCondition(RolePermissionPoTableDef.ROLE_PERMISSION_PO.ROLE_ID.eq(dto.getRoleId()));

        ArrayList<RolePermissionPo> rolePermissionPos = new ArrayList<>();

        dto.getPermissionId().forEach(p -> {
            RolePermissionPo rolePermissionPo = new RolePermissionPo();
            rolePermissionPo.setRoleId(dto.getRoleId());
            rolePermissionPo.setPermissionId(p);
            rolePermissionPos.add(rolePermissionPo);
        });

        return  rolePermissionMapper.insertBatch(rolePermissionPos);
    }

}
