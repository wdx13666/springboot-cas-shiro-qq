package com.wdx.flyleg.realm;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wdx.flyleg.pojo.SysPermission;
import com.wdx.flyleg.pojo.SysUser;
import com.wdx.flyleg.service.SysPermissionService;
import com.wdx.flyleg.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
        List<SysPermission> sysPermissions = permissionService.selectList(null);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addObjectPermission((Permission) sysPermissions);
        log.info("doGetAuthorizationInfo");
        return info;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_name", token.getUsername()));
        if (sysUser == null) {
            return null;
        }
        List<SysPermission> menus = permissionService.selectList(null);
        sysUser.setMenus(menus);
        log.info("doGetAuthenticationInfo");
        return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(),getName());
//        ByteSource.Util.bytes(sysUser.getSalt()),
    }
}
