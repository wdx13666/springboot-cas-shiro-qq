package com.wdx.flyleg.service;

import com.baomidou.mybatisplus.service.IService;
import com.wdx.flyleg.pojo.SysPermission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
public interface SysPermissionService extends IService<SysPermission> {

    public List<SysPermission> selectPermissionsByRoleId(Integer roleId);


}
