package com.wdx.flyleg.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wdx.flyleg.mapper.SysUserMapper;
import com.wdx.flyleg.mapper.SysUserRoleMapper;
import com.wdx.flyleg.pojo.SysUser;
import com.wdx.flyleg.pojo.SysUserRole;
import com.wdx.flyleg.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Transactional
    @Override
    public int insertSysUserAndUserRole(SysUser sysUser, Long[] ids) {
        Integer insert = baseMapper.insert(sysUser);
        SysUserRole sysUserRole = new SysUserRole();
        if (ids != null){
            for (Long id : ids){
                sysUserRole.setUserId(sysUser.getUserId());
                sysUserRole.setRoleId(id);
                sysUserRoleMapper.insert(sysUserRole);
            }
        }

        return insert;
    }

    @Override
    public SysUser selectUserAndRoleById(Long id) {
        return baseMapper.selectUserAndRoleById(id);
    }
}
