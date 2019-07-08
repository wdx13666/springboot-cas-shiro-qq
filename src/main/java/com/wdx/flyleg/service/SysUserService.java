package com.wdx.flyleg.service;

import com.baomidou.mybatisplus.service.IService;
import com.wdx.flyleg.pojo.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
public interface SysUserService extends IService<SysUser> {

    public int insertSysUserAndUserRole(SysUser sysUser, Long[] ids);

    public SysUser selectUserAndRoleById(Long id);

}
