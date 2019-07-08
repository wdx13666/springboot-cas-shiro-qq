package com.wdx.flyleg.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wdx.flyleg.mapper.SysPermissionMapper;
import com.wdx.flyleg.pojo.SysPermission;
import com.wdx.flyleg.service.SysPermissionService;
import org.springframework.stereotype.Service;

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
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> selectPermissionsByRoleId(Integer roleId) {
        List<SysPermission> sysPermissions = baseMapper.selectPermissionsByRoleId(roleId);
        return sysPermissions;
    }


}
