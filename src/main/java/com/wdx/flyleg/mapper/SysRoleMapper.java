package com.wdx.flyleg.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wdx.flyleg.pojo.SysRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
    public interface SysRoleMapper extends BaseMapper<SysRole> {

    public List<SysRole> getSysRole(Long id);

    public SysRole selectByRoleId(Long roleId);

}
