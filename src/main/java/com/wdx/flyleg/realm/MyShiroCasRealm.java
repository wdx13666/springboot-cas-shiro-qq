package com.wdx.flyleg.realm;

import com.wdx.flyleg.mapper.SysUserMapper;
import com.wdx.flyleg.pojo.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2018/12/27
 *
 */
public class MyShiroCasRealm extends CasRealm {

    @Autowired
    private SysUserMapper userMapper;


    private static final Logger logger = LoggerFactory.getLogger(MyShiroCasRealm.class);

    @Value("${cas.server-url}")
    public  String casServerUrlPrefix;

    @Value("${cas.service}")
    public  String shiroServerUrlPrefix;

    @PostConstruct
    public void initProperty(){
        // cas server地址
        setCasServerUrlPrefix(casServerUrlPrefix);
        // 客户端回调地址
        setCasService(shiroServerUrlPrefix + "/cas");
    }

    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中(不用了，随时可以获取的)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {

        AuthenticationInfo authc = super.doGetAuthenticationInfo(token);

        String account = (String) authc.getPrincipals().getPrimaryPrincipal();

        System.out.println("========"+account+"==========");

        SysUser user=new SysUser();
//        user.setLdapAccount(account);

//        user = userMapper.queryCasLogin(user);
        //将用户信息存入session中
        SecurityUtils.getSubject().getSession().setAttribute("user", user);

        return authc;
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * 本例中该方法的调用时机为需授权资源被访问时
     * 并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * 如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        logger.info("##################Shiro权限认证##################");

        SysUser user = new SysUser();
        if(user!=null){
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //给用户添加角色（让shiro去验证）
            Set<String> roleNames = new HashSet<>();
            if(user.getUserName().equals("admin")){
                roleNames.add("admin");
            }
            info.setRoles(roleNames);
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }

}
