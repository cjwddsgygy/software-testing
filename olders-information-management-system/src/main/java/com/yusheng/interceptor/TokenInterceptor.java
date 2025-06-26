package com.yusheng.interceptor;

import com.yusheng.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 令牌校验的拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
//    在目标资源方法运行之前 - 返回值：true 放行， false 不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        1.获取到请求路径
        String requestURI = request.getRequestURI();

//        2.判断是否是登录请求，如果路径中包含 /adminLogin、/careWorkerLogin、/elderLogin，说明是登陆操作，放行
        if (requestURI.contains("/adminLogin") || requestURI.contains("careWorkerLogin")
                || requestURI.contains("elderLogin")) {
            return true;
        }

//        3.获取请求头中的token
        String token = request.getHeader("token");

//        4.判断token是否存在，若果不存在，说明用户没有登录，返回错误信息（响应401状态码）
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

//        5.如果token存在，校验令牌，如果校验失败 -> 返回错误信息（响应401状态码）
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

//        6.校验通过，放行
        return true;
    }
}
