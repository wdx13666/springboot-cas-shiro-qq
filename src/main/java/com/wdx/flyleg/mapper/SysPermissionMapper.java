package com.wdx.flyleg.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wdx.flyleg.pojo.SysPermission;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    public List<SysPermission> selectPermissionsByRoleId(Integer roleId);
}
