// 文件路径: backend/src/main/java/com/yusheng/config/SecurityConfig.java
package com.yusheng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启方法级安全注解支持，为未来扩展做准备
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(restfulAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. 公开访问路径
                        .requestMatchers(
                                "/api/login", // 统一登录接口
                                "/api/files/**",      // 文件上传接口
                                "/uploads/**"// 静态文件访问路径
                        ).permitAll()

                        // 2. OPTIONS 预检请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅✅✅ 核心修复：精细化授权 ✅✅✅

                        // 2. 护工和管理员可以进行写操作的核心业务模块
                        .requestMatchers(HttpMethod.POST, "/api/elders/**", "/api/beds/**", "/api/health-records/**", "/api/expenses/**").hasAnyRole("ADMIN", "CAREWORKER")
                        .requestMatchers(HttpMethod.PUT, "/api/elders/**", "/api/beds/**", "/api/health-records/**", "/api/expenses/**").hasAnyRole("ADMIN", "CAREWORKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/elders/**", "/api/beds/**", "/api/health-records/**", "/api/expenses/**").hasAnyRole("ADMIN", "CAREWORKER")

                        // 3. 管理员专属的写操作模块
                        .requestMatchers(HttpMethod.POST, "/api/careWorkers/**", "/api/settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/careWorkers/**", "/api/settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/careWorkers/**", "/api/settings/**").hasRole("ADMIN")

                        // 4. 所有登录用户都可以执行 GET 请求 (最宽泛的读权限)
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()

                        // 其他所有未匹配到的请求，也要求认证 (作为安全兜底)
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS配置Bean (保持不变)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}