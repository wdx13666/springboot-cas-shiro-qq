package com.wdx.flyleg.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.wdx.flyleg.pojo.SysUser;
import com.wdx.flyleg.properties.OAuthProperties;
import com.wdx.flyleg.realm.MyShiroCasRealm;
import com.wdx.flyleg.service.SysUserService;
import com.wdx.flyleg.utils.HttpsUtils;
import com.wdx.flyleg.vo.QQDTO;
import com.wdx.flyleg.vo.QQOpenidDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private OAuthProperties oauth;
    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /*
     *
     * get请求，登录页面跳转
     * @return
     */
    @GetMapping("/login")
    public String login() {
        //如果已经认证通过，直接跳转到首页
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }
        return "/login";
    }

    /*
     *
     * post表单提交，登录
     * @param username
     * @param password
     * @param model
     * @return
     */
    @PostMapping("/login")
    public Object login(String username, String password, Model model) {
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            //shiro帮我们匹配密码什么的，我们只需要把东西传给它，它会根据我们在UserRealm里认证方法设置的来验证
            user.login(token);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            //账号不存在和下面密码错误一般都合并为一个账号或密码错误，这样可以增加暴力破解难度
            model.addAttribute("message", "账号不存在！");
        } catch (DisabledAccountException e) {
            model.addAttribute("message", "账号未启用！");
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("message", "密码错误！");
        } catch (Throwable e) {
            model.addAttribute("message", "未知错误！");
        }
        return "/login";
    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "/login";
    }


    //QQ登陆对外接口，只需将该接口放置html的a标签href中即可
    @GetMapping("/login/qq")
    public void loginQQ(HttpServletResponse response) {
        try {
            response.sendRedirect(oauth.getQq().getCode_callback_uri() + //获取code码地址
                    "?client_id=" + oauth.getQq().getClient_id()//appid
                    + "&state=" + UUID.randomUUID() + //这个说是防攻击的，就给个随机uuid吧
                    "&redirect_uri=" + oauth.getQq().getRedirect_uri() +//这个很重要，这个是回调地址，即就收腾讯返回的code码
                    "&response_type=code");//授权模式，授权码模式
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收回调地址带过来的code码
    @GetMapping("/authorize/qq")
    public String authorizeQQ(Map<String, String> msg, String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", oauth.getQq().getRedirect_uri());
        params.put("client_id", oauth.getQq().getClient_id());
        params.put("client_secret", oauth.getQq().getClient_secret());
        //获取access_token如：access_token=9724892714FDF1E3ED5A4C6D074AF9CB&expires_in=7776000&refresh_token=9E0DE422742ACCAB629A54B3BFEC61FF
        String result = HttpsUtils.doGet(oauth.getQq().getAccess_token_callback_uri(), params);
        //对拿到的数据进行切割字符串
        String[] strings = result.split("&");
        //切割好后放进map
        Map<String, String> reulsts = new HashMap<>();
        for (String str : strings) {
            String[] split = str.split("=");
            if (split.length > 1) {
                reulsts.put(split[0], split[1]);
            }
        }
        //到这里access_token已经处理好了
        //下一步获取openid，只有拿到openid才能拿到用户信息
        String openidContent = HttpsUtils.doGet(oauth.getQq().getOpenid_callback_uri() + "?access_token=" + reulsts.get("access_token"));
        //接下来对openid进行处理
        //截取需要的那部分json字符串
        String openid = openidContent.substring(openidContent.indexOf("{"), openidContent.indexOf("}") + 1);
        Gson gson = new Gson();
        //将返回的openid转换成DTO
        QQOpenidDTO qqOpenidDTO = gson.fromJson(openid, QQOpenidDTO.class);

        //接下来说说获取用户信息部分
        //登陆的时候去数据库查询用户数据对于openid是存在，如果存在的话，就不用拿openid获取用户信息了，而是直接从数据库拿用户数据直接认证用户，
        // 否则就拿openid去腾讯服务器获取用户信息，并存入数据库，再去认证用户
        //下面关于怎么获取用户信息，并登陆
        params.clear();
        params.put("access_token", reulsts.get("access_token"));//设置access_token
        params.put("openid", qqOpenidDTO.getOpenid());//设置openid
        params.put("oauth_consumer_key", qqOpenidDTO.getClient_id());//设置appid
        //获取用户信息
        String userInfo = HttpsUtils.doGet(oauth.getQq().getUser_info_callback_uri(), params);
        QQDTO qqDTO = gson.fromJson(userInfo, QQDTO.class);
        //根据数据库进行比对
        SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_name", qqDTO.getNickname()));
        //不存在保存用户信息
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setFullName(qqDTO.getNickname());
            sysUser.setUserName(qqDTO.getNickname());
            sysUser.setOpenId(qqOpenidDTO.getOpenid());
            sysUser.setPassword(qqOpenidDTO.getOpenid());
            sysUserService.insert(sysUser);
        }
        //这里拿用户昵称，作为用户名，openid作为密码（正常情况下，在开发时候用openid作为用户名，再自己定义个密码就可以了）
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(qqDTO.getNickname(), qqOpenidDTO.getOpenid()));
        } catch (Exception e) {
            msg.put("msg", "第三方登陆失败,请联系管理！");
            log.error(e.getMessage());
            return "login";
        }
        return "redirect:/index";
    }


}
