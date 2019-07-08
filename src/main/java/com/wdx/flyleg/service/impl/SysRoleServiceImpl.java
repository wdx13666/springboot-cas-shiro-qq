package com.wdx.flyleg.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wdx.flyleg.mapper.SysRoleMapper;
import com.wdx.flyleg.pojo.SysRole;
import com.wdx.flyleg.pojo.SysRolePermission;
import com.wdx.flyleg.service.SysRolePermissionService;
import com.wdx.flyleg.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRolePermissionService rolePermissionService;

    @Transactional
    @Override
    public Boolean insertRoleAndPermission(SysRole sysRole, Long[] ids) {
        baseMapper.insert(sysRole);
        if (ids != null){
            rolePermission(sysRole, ids);

        }

        return true;
    }

    @Override
    public List<SysRole> getSysRole(Long id) {
        List<SysRole> sysRole = baseMapper.getSysRole(id);
        return sysRole;
    }

    @Transactional
    @Override
    public Boolean updateByRoleId(SysRole sysRole, Long[] ids) {
        baseMapper.updateById(sysRole);
        if (ids != null){
            rolePermissionService.deleteById(sysRole.getRoleId());
            rolePermission(sysRole, ids);
        }
        return true;
    }

    private void rolePermission(SysRole sysRole, Long[] ids) {
        for (Long id : ids){
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setRoleId(sysRole.getRoleId());
            rolePermission.setPermissionId(id);
            rolePermissionService.insert(rolePermission);
        }
    }
}
