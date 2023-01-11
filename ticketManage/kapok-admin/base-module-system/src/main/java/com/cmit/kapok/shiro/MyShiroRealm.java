package com.cmit.kapok.shiro;

import com.cmit.kapok.shiro.jwt.JwtToken;
import com.cmit.kapok.system.utils.TokenUtil;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.entity.user.SysUser;
import com.cmit.kapok.system.entity.user.TokenUserDAO;
import com.cmit.kapok.system.entity.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cmit.kapok.constants.RedisPrefixConstants.SYSTEM_USER_TOKEN_PREFIX;

@Component
public class MyShiroRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SysUserService userService;
    @Resource
    private TokenUtil tokenUtil;
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if(principalCollection.getPrimaryPrincipal().getClass().equals(SysUser.class)){
            SysUser currentUser = (SysUser)principalCollection.getPrimaryPrincipal();
            int id = currentUser.getId();
            User result = userService.queryById(id);
            //获取角色信息
            List roles = new ArrayList<String>();
            //如果有多条角色信息，分开添加
            String rolesStr = result.getRole();
            if(rolesStr.contains(",")){
                roles = Arrays.asList(rolesStr.split(","));
            }
            else {
                roles.add(rolesStr);
            }
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.addRoles(roles);
            logger.warn(String.format("[MyShiroRealm/doGetAuthorizationInfo]授权 - %s,(%s)",result.getRealName(),result.getRole()));
            return authorizationInfo;
        }else {
            return null;
        }
    }

    /**
     * 认证，验证当前登录的Subject
     * LoginController.login 方法中调用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        TokenUserDAO tokenUserDAO = null;
        try{
            tokenUserDAO = tokenUtil.verifyToken(token);
        }catch (Exception e){
            if(e instanceof MalformedJwtException){
                logger.error("token invalid",e);
                throw new AuthenticationException("token invalid",e);
            }else if(e instanceof ExpiredJwtException){
                throw new AuthenticationException("token expired",e);
            }else if(e instanceof SignatureException){
                logger.error("token invalid",e);
                throw new AuthenticationException("token invalid",e);
            }else{
                logger.error("unexpected error",e);
                throw new AuthenticationException("unexpected error",e);
            }
        }
        logger.debug("用户认证 - {}({})",tokenUserDAO.getRealName(),tokenUserDAO.getUserName());
        if(stringRedisTemplate.hasKey(SYSTEM_USER_TOKEN_PREFIX+token)){
            return new SimpleAuthenticationInfo(token, token, getName());
        }
        return null;
    }

}