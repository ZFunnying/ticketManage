package com.cmit.kapok.shiro;

import com.cmit.kapok.shiro.filter.JwtFilter;
import com.cmit.kapok.shiro.filter.RolesOrFilter;
import com.cmit.kapok.system.api.permission.PermissionService;
import com.cmit.kapok.system.entity.permission.Permission;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
	@Resource
	private PermissionService permissionService;
	@Value("${spring.profiles.active}")
	private String env;//当前激活的配置文件
	@Value("${myconfig.excludePathPatterns}")
	private String excludePathPatterns;// 放行标志
	@Bean(name="shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		shiroFilterFactoryBean.setLoginUrl("/redirect/login");//登录连接
		shiroFilterFactoryBean.setSuccessUrl("/dashboard");//登录成功后跳转的连接
		shiroFilterFactoryBean.setUnauthorizedUrl("/redirect/401"); //未授权跳转页面

		//自定义filter
		Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		filtersMap.put("OrRoles", new RolesOrFilter());
		filtersMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filtersMap);

		//定义shiro过滤链
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		String[] patterns = excludePathPatterns.split(",");
		for(String pattern:patterns){
			if(Arrays.asList("/redirect/**").contains(pattern)){
				continue;
			}else {
				filterChainDefinitionMap.put(pattern, "anon");
			}
		}
		// 动态加载一次权限
		List<Permission> permissionList = permissionService.list();
		for(Permission permission:permissionList){
			if (StringUtils.isNotEmpty(permission.getUrl())) {
				String roles = "jwt,OrRoles[" + permission.getRoles()+ "]";
				filterChainDefinitionMap.put(permission.getUrl(),roles);
			}
		}
		filterChainDefinitionMap.put("/**", "jwt");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager(){
		//使用默认的安全管理器
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//加入自定义的安全领域
		securityManager.setRealm(securityRealm());
		// 关闭Shiro自带的session
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		SecurityUtils.setSecurityManager(securityManager);
		return securityManager;
	}

	@Bean
	public MyShiroRealm securityRealm(){
		MyShiroRealm securityRealm = new MyShiroRealm();
//		securityRealm.setCredentialsMatcher(hashedCredentialsMatcher());//凭证匹配器
		securityRealm.setCachingEnabled(false);//不使用缓存
		return securityRealm;
	}

//	@Bean
//	public HashedCredentialsMatcher hashedCredentialsMatcher(){
//		HashedCredentialsMatcher  hashedCredentialsMatcher = new HashedCredentialsMatcher();
//		hashedCredentialsMatcher.setHashAlgorithmName("md5");//使用MD5散列算法
//		hashedCredentialsMatcher.setHashIterations(2);//散列次数，这里等于1次MD5
//		hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);  //散列后密码为16进制，要与生成密码时一致。false 表示Base64编码
//		return hashedCredentialsMatcher;
//	}


}
