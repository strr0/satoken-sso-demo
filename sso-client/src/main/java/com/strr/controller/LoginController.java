package com.strr.controller;

import cn.dev33.satoken.sso.SaSsoProcessor;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    // 当前是否登录
    @RequestMapping("/sso/isLogin")
    public Object isLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

    // 返回SSO认证中心登录地址
    @RequestMapping("/sso/getSsoAuthUrl")
    public SaResult getSsoAuthUrl(String clientLoginUrl) {
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(clientLoginUrl, "");
        return SaResult.data(serverAuthUrl);
    }

    // 根据ticket进行登录
    @RequestMapping("/sso/doLoginByTicket")
    public SaResult doLoginByTicket(String ticket) {
        Object loginId = SaSsoProcessor.instance.checkTicket(ticket, "/sso/doLoginByTicket");
        if(loginId != null) {
            StpUtil.login(loginId);
            return SaResult.data(StpUtil.getTokenValue());
        }
        return SaResult.error("无效ticket：" + ticket);
    }

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
}
