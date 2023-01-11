package com.cmit.kapok.system.service.permission;

import com.cmit.kapok.system.api.permission.PermissionService;
import com.cmit.kapok.system.api.permission.ShiroService;
import com.cmit.kapok.system.entity.permission.Permission;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShiroServiceImpl implements ShiroService {
    private final Logger logger = LoggerFactory.getLogger(ShiroServiceImpl.class);
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private PermissionService permissionService;
    @Value("${myconfig.excludePathPatterns}")
    private String excludePathPatterns;// 放行标志
    /**
     * 初始化权限
     */
    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        //定义shiro过滤链

        // 新建权限链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 清空原来的权限链
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        // 把配置的权限先添加进去
        String[] patterns = excludePathPatterns.split(",");
        for(String pattern:patterns){
            if(Arrays.asList("/redirect/**").contains(pattern)){
                continue;
            }else {
                filterChainDefinitionMap.put(pattern, "anon");
            }
        }
        // 添加数据库里的权限
        List<Permission> permissionList = permissionService.list();
        for(Permission permission:permissionList){
            if (StringUtils.isNotEmpty(permission.getUrl())) {
                String roles = "jwt,OrRoles[" + permission.getRoles()+ "]";
                filterChainDefinitionMap.put(permission.getUrl(),roles);
            }
        }
        filterChainDefinitionMap.put("/**", "jwt");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    @Override
    public void updatePermission() {

        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
                        .getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            Map chain = loadFilterChainDefinitions();
            logger.warn("new permission chain:"+chain.toString());
            shiroFilterFactoryBean
                    .setFilterChainDefinitionMap(chain);

            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean
                    .getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

            logger.warn("updatePermission() --> 更新权限成功.");
        }
    }
}
