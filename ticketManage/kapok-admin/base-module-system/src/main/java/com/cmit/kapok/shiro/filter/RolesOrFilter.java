package com.cmit.kapok.shiro.filter;

import com.cmit.kapok.system.utils.SpringBeanFactoryUtils;
import com.cmit.kapok.system.utils.TokenUtil;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.entity.user.TokenUserDAO;
import com.cmit.kapok.system.entity.user.User;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RolesOrFilter extends RolesAuthorizationFilter {
    private final Logger logger = LoggerFactory.getLogger(RolesOrFilter.class);

    @Autowired
    private SysUserService sysUserService ;
    @Autowired
    private TokenUtil tokenUtil ;
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                   Object mappedValue){
        // 通过这个方式注入bean
        if (sysUserService == null) {
            sysUserService = SpringBeanFactoryUtils.getBean(SysUserService.class);
        }
        // 通过这个方式注入bean
        if (tokenUtil == null) {
            tokenUtil = SpringBeanFactoryUtils.getBean(TokenUtil.class);
        }
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);

        TokenUserDAO userInfo = tokenUtil.getTokenUserInfo(httpServletRequest);
        logger.info("权限验证 - [{}] - {}({})",httpServletRequest.getRequestURI(),userInfo.getRealName(),userInfo.getUserName());

        final String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }

        int id = Integer.parseInt(userInfo.getUserId());

        User result = sysUserService.queryById(id);
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

        for (String roleName : rolesArray) {
            if (roles.contains(roleName)) {
                return true;
            }
        }

        return false;
    }
}