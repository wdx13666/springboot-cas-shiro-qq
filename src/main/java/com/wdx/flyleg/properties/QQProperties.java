package com.wdx.flyleg.properties;

import lombok.Data;

/**
 * QQ第三方登陆参数类
 */
@Data
public class QQProperties {
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code_callback_uri;
    private String access_token_callback_uri;
    private String openid_callback_uri;
    private String user_info_callback_uri;

}
