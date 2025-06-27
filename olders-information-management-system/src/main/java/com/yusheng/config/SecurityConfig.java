package com.yusheng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 启用并配置CORS，它会使用我们上面在WebConfig中定义的全局配置
                .cors(withDefaults())

                // 禁用CSRF
                .csrf(csrf -> csrf.disable())

                // 配置URL授权
                .authorizeHttpRequests(auth -> auth
                        // 放行登录接口
                        .requestMatchers("/admin/login").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )

                // 设置为无状态会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}