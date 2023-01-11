package com.cmit.kapok.shiro.filter;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.shiro.jwt.JwtToken;
import com.cmit.kapok.system.utils.SpringBeanFactoryUtils;
import com.cmit.kapok.system.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static com.cmit.kapok.constants.Constants.TOKEN_EXPIRE_TIME;
import static com.cmit.kapok.constants.RedisPrefixConstants.SYSTEM_USER_TOKEN_PREFIX;
import static com.cmit.kapok.constants.RedisPrefixConstants.TOKEN_REFLASH_PREFIX;

public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof IllegalArgumentException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确";
                } else if (throwable instanceof MalformedJwtException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确";
                } else if (throwable instanceof ExpiredJwtException) {
                    boolean pass = false;
                    try {
                        pass = this.refreshToken(request,response);
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                    if(pass){
                        return true;
                    } else {
                        // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                        msg = "Token已过期";
                        this.responseResult(response,ResultGenerator.genTokenExpiredResult(msg));
                        return false;
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                logger.error("认证失败",e);
                this.responseResult(response,ResultGenerator.genFailResult(msg));
                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpServletRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpServletRequest.getRequestURI();
            logger.error("当前请求 {} Authorization属性(Token)为空 请求类型 {}", requestURI, httpMethod);
            this.responseResult(response,ResultGenerator.genFailResult("请先登录"));
            return false;
        }
        return true;
    }

    private boolean refreshToken(ServletRequest request, ServletResponse response) throws JsonProcessingException {
        String token = this.getAuthzHeader(request);
        // 通过这个方式注入bean
        if (stringRedisTemplate == null) {
            stringRedisTemplate = SpringBeanFactoryUtils.getBean(StringRedisTemplate.class);
        }
        if (tokenUtil == null) {
            tokenUtil = SpringBeanFactoryUtils.getBean(TokenUtil.class);
        }
        if (objectMapper == null) {
            objectMapper = SpringBeanFactoryUtils.getBean(ObjectMapper.class);
        }
        // 看token是否已经过了两天
        if(stringRedisTemplate.hasKey(SYSTEM_USER_TOKEN_PREFIX+token)){
            String userInfoString = stringRedisTemplate.opsForValue().get(SYSTEM_USER_TOKEN_PREFIX+token);
            JsonNode userInfo =  objectMapper.readTree(userInfoString);
            String id = userInfo.get("userId").asText();
            String userName = userInfo.get("userName").asText();
            String realName = userInfo.get("realName").asText();
            String jwt = tokenUtil.createJWT(id, userName, realName,1000*(TOKEN_EXPIRE_TIME));
            // 5分钟后销毁原来的token
            stringRedisTemplate.expire(SYSTEM_USER_TOKEN_PREFIX+token,5,TimeUnit.MINUTES);
            // 新建新的token
            if(stringRedisTemplate.hasKey(TOKEN_REFLASH_PREFIX+token)){
                jwt = stringRedisTemplate.opsForValue().get(TOKEN_REFLASH_PREFIX+token);
            }else {
                stringRedisTemplate.opsForValue().set(SYSTEM_USER_TOKEN_PREFIX+jwt,objectMapper.writeValueAsString(userInfo),2, TimeUnit.DAYS); // token请求过期时间
                // 设置token刷新标记，避免并发请求
                stringRedisTemplate.opsForValue().set(TOKEN_REFLASH_PREFIX+token,jwt,30,TimeUnit.SECONDS);
                JwtToken jwtToken = new JwtToken(jwt);
                this.getSubject(request, response).login(jwtToken);
            }
            // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            httpServletResponse.setHeader("Authorization", jwt);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return true;
        }else {
            return false;
        }
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response){
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JwtToken token = new JwtToken(this.getAuthzHeader(request));
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 跨域已经在OriginFilter处全局配置
        /*HttpServletRequest httpServletRequest = WebUtils.toHtwtp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }*/
        return super.preHandle(request, response);
    }

    private void responseResult(ServletResponse response, Result result) {
        if (objectMapper == null) {
            objectMapper = SpringBeanFactoryUtils.getBean(ObjectMapper.class);
        }
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = objectMapper.writeValueAsString(result);
            out.append(data);
        } catch (IOException e) {
            logger.error("直接返回Response信息出现IOException异常:{}", e.getMessage());
            throw new RuntimeException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }
}
