package com.wdx.flyleg.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wdx.flyleg.pojo.SysUser;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    public SysUser selectUserAndRoleById(Long id);

    public Set<SysUser> selectByRoleId(Long roleId);

}
