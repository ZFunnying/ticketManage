package com.cmit.kapok.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordLoginTypeToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 7134536615448037793L;
    /**
     *登陆类型
     */
    private String loginType;

    public UsernamePasswordLoginTypeToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}