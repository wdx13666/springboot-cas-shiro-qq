package com.wdx.flyleg.service;

import com.baomidou.mybatisplus.service.IService;
import com.wdx.flyleg.pojo.SysRolePermission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

   public boolean insertRolePermission(List<SysRolePermission> rolePermissions, Long id);
}
