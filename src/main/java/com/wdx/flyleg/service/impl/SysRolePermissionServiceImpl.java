package com.wdx.flyleg.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wdx.flyleg.mapper.SysRolePermissionMapper;
import com.wdx.flyleg.pojo.SysRolePermission;
import com.wdx.flyleg.service.SysRolePermissionService;
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
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

    @Autowired
    private SysRolePermissionService rolePermissionService;

    @Transactional
    @Override
    public boolean insertRolePermission(List<SysRolePermission> rolePermissions, Long id) {
        Integer integer = baseMapper.deleteById(id);
        boolean b = rolePermissionService.insertBatch(rolePermissions);
        return b;

    }


}
