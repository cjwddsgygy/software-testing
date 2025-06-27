package com.yusheng.config;

import com.yusheng.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器，拦截所有请求，但放行登录接口
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/admin/login");
    }

    // ---  在这里添加全局CORS配置  ---
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有接口生效
                .allowedOriginPatterns("*") // 允许所有源，更安全的做法是 .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowCredentials(true) // 允许携带cookie
                .maxAge(3600) // 预检请求的有效期
                .allowedHeaders("*"); // 允许所有请求头
    }
}
