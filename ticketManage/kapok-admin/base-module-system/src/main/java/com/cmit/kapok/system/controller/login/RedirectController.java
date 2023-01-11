package com.cmit.kapok.system.controller.login;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "shiro重定向")
@RequestMapping("/redirect")
public class RedirectController {
    private final Logger logger = LoggerFactory.getLogger(RedirectController.class);
    @RequestMapping(value = "/401")
    public Result unauthorizedUrl(){
        logger.warn("this user is unauthorized,redirect to 401");
        return ResultGenerator.genFailResult("当前用户无授权");
    }

    @RequestMapping(value = "/login")
    public Result login(){
        logger.warn("shiro login status failed,redirect to login");
        return ResultGenerator.genReLoginResult("跳转到登录页");
    }
}
