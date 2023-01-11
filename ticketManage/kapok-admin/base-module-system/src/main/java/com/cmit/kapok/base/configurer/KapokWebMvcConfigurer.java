package com.cmit.kapok.base.configurer;


import com.cmit.kapok.base.core.ErrorCode;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultCode;
import com.cmit.kapok.base.core.ServiceException;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.utils.TokenUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cmit.kapok.constants.RedisPrefixConstants.SESSION_LOCK_PREFIX;

// TODO: 待修改

/**
 * Spring MVC 配置
 */
@Configuration
public class KapokWebMvcConfigurer extends WebMvcConfigurationSupport {

    private final Logger logger = LoggerFactory.getLogger(KapokWebMvcConfigurer.class);
    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件
    @Value("${myconfig.excludePathPatterns}")
    private String excludePathPatterns;// 放行标志
    @Autowired
    private SysUserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(stringHttpMessageConverter);

        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 出参转换
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                    logger.warn(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    logger.error("接口 [" + request.getRequestURI() + "]" + e.getMessage());
                    // api接口返回400
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                } else if (e instanceof MethodArgumentNotValidException) {
                    logger.error("接口 [" + request.getRequestURI() + "]" + e.getMessage());
                    // api接口返回400
                    List<ObjectError> allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
                    StringBuilder sb = new StringBuilder();
                    for (ObjectError error : allErrors) {
                        sb.append(error.getDefaultMessage()).append(";");
                    }
                    result.setCode(ResultCode.FAIL).setMessage(sb.toString());
                } else if (e instanceof NullPointerException) {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 空指针异常");
                    logger.error(String.format("接口 [%s] 空指针异常,错误信息:%s", request.getRequestURI(), e.toString()), e);
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(String.format("接口 [%s] 内部错误，请联系管理员,错误信息：%s", request.getRequestURI(), message), e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }

        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/druid/**").addResourceLocations("classpath:/support/http/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置你要允许的网站域名，如果全允许则设为 *
        config.addAllowedOrigin("*");
        // 如果要限制 HEADER 或 METHOD 请自行更改
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 这个顺序很重要哦，为避免麻烦请设置在最前
        bean.setOrder(0);
        return bean;
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] uri = excludePathPatterns.split(",");
//        if("dev".equals(env)){
//            uri = new String[]{"/**"};
//        }
        //接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 验证token
                ErrorCode pass = validateToken(request);
                if (pass.getValue() == 20000) {
                    return true;
                } else if (ErrorCode.TOKEN_EXPIRED.equals(pass.getValue())) {
                    logger.error(pass.getDesc() + "，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request), objectMapper.writeValueAsString(request.getParameterMap()));

                    Result result = new Result();
                    result.setCode(pass.getValue()).setMessage(pass.getDesc());
                    responseResult(response, result);
                    return false;
                } else if (ErrorCode.TOKEN_ERROR.equals(pass.getValue())) {
                    logger.error(pass.getDesc() + "，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request), objectMapper.writeValueAsString(request.getParameterMap()));

                    Result result = new Result();
                    result.setCode(pass.getValue()).setMessage(pass.getDesc());
                    responseResult(response, result);
                    return false;
                } else if (ErrorCode.TOKEN_INVALID.equals(pass.getValue())) {
                    logger.error(pass.getDesc() + "，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request), objectMapper.writeValueAsString(request.getParameterMap()));

                    Result result = new Result();
                    result.setCode(pass.getValue()).setMessage(pass.getDesc());
                    responseResult(response, result);
                    return false;
                } else {
                    logger.error("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request), objectMapper.writeValueAsString(request.getParameterMap()));
                    Result result = new Result();
                    result.setCode(ResultCode.UNAUTHORIZED).setMessage("签名认证失败");
                    responseResult(response, result);
                    return false;
                }
            }
        }).excludePathPatterns("/**");
        /**
         * @Author lizhitao
         * @Description 这里实现第二个拦截器，不影响第一个拦截器逻辑
         **/
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String url = request.getRequestURI();
                if (stringRedisTemplate.opsForValue().setIfAbsent(
                        SESSION_LOCK_PREFIX + request.getSession().getId() + "_" + url, request.getSession().getId(), 500, TimeUnit.MILLISECONDS)) {
                    return true;
                } else {
                    Result result = new Result();
                    result.setCode(ResultCode.FAIL).setMessage("请求太频繁啦，请稍后再试");
                    responseResult(response, result);
                    logger.error("session request too frequently:{},{},{}", url
                            , request.getSession().getId(), getIpAddress(request));
                    return false;
                }
            }
        });
    }

    private ErrorCode validateToken(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        try {
            tokenUtil.parseJWT(token);
        } catch (ExpiredJwtException e) {
            logger.error("token expired", e);
            return ErrorCode.TOKEN_EXPIRED;
        } catch (Exception e) {
            logger.error("token invaild", e);
            return ErrorCode.TOKEN_ERROR;
        }
        return ErrorCode.SUCCESS;
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");//排除sign参数
        Collections.sort(keys);//排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//去除最后一个'&'

        String secret = "Potato";//密钥，自己修改
        String sign = DigestUtils.md5Hex(linkString + secret);//混合密钥md5

        return StringUtils.equals(sign, requestSign);//比较
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}
