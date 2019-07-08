package com.wdx.flyleg.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
@Data
@TableName("sf_sys_role")
public class SysRole {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
    /**
     * 角色名
     */
    private String roleName;

    @TableField(exist = false)
    private Set<SysUser> sysUsers;

    @TableField(exist = false)
    private List<SysPermission> permissions;




}
