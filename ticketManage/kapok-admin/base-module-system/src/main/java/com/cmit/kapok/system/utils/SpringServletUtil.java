package com.cmit.kapok.system.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *  Spring Boot HTTP 相关工具
 */

public class SpringServletUtil {

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = 
                ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

}