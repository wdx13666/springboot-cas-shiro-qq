package com.wdx.flyleg.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wdx
 * @since 2019-04-22
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sf_sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**
     * 账号
     */
    private String userName;

    @TableField(exist = false)
    private List<SysRole> sysRoles;

    /**
     * 姓名
     */
    private String fullName;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 菜单
     */

    /**
     * qq openId
     */
    private String openId;

    @TableField(exist = false)
    private List<SysPermission> menus;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
