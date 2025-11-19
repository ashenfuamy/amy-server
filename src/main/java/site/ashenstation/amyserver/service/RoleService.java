package site.ashenstation.amyserver.service;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.ashenstation.amyserver.config.exception.BadRequestException;
import site.ashenstation.amyserver.dto.RoleDto;
import site.ashenstation.amyserver.entity.RolePo;
import site.ashenstation.amyserver.entity.table.RolePoTableDef;
import site.ashenstation.amyserver.mapper.RoleMapper;
import site.ashenstation.amyserver.vo.RoleVo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;

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
        List<RoleVo> roleVos = QueryChain.of(roleMapper)
                .select()
                .listAs(RoleVo.class);

        return roleVos;
    }

}
