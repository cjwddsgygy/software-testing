// 文件路径: backend/src/main/java/com/yusheng/config/SecurityConfig.java
package com.yusheng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 和 session，因为我们使用 JWT
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅✅✅ 核心授权逻辑，全部放在这里 ✅✅✅
                .authorizeHttpRequests(auth -> auth
                        // 1. 首先，无条件放行所有 OPTIONS 预检请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. 明确列出所有需要匿名访问的路径
                        .requestMatchers(
                                "/admin/login",
                                "/careWorkerLogin",
                                "/elderLogin",
                                "/api/elders/**" // 确保老人接口在这里被放行
                        ).permitAll()

                        // 3. 除了上面明确放行的，其他所有请求都需要认证
                        .anyRequest().authenticated()
                )

                // 禁用默认的登录页和 HTTP Basic 认证
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 将我们的 JWT 过滤器添加到认证流程中
        // 它会在处理需要认证的请求时，解析 Token
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}