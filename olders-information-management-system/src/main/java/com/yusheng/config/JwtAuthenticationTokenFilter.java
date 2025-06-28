// 文件路径: backend/src/main/java/com/yusheng/config/JwtAuthenticationTokenFilter.java
package com.yusheng.config;

import com.yusheng.pojo.Admin;
import com.yusheng.pojo.CareWorkerInfo; // 假设您有这个类
import com.yusheng.pojo.ElderInfo;   // 假设您有这个类
import com.yusheng.pojo.LoginUser;
import com.yusheng.service.AdminService;
import com.yusheng.service.CareWorkersSelfService; // 注入护工服务
import com.yusheng.service.ElderSelfService;       // 注入老人服务
import com.yusheng.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CareWorkersSelfService careWorkersSelfService;
    @Autowired
    private ElderSelfService elderSelfService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // (DEBUG 日志保持不变, 用于观察)

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Claims claims = JwtUtils.parseToken(jwt);

            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Integer userId = claims.get("id", Integer.class);
                String userType = claims.get("userType", String.class);

                if (userId != null && StringUtils.hasText(userType)) {
                    LoginUser loginUser = loadUser(userId, userType);

                    if (loginUser != null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 根据用户ID和用户类型，加载真实的用户信息并包装成 LoginUser
     */
    private LoginUser loadUser(Integer userId, String userType) {
        Object principalUser = null;
        List<String> permissions = Collections.emptyList(); // 默认为空权限

        switch (userType) {
            case "ADMIN":
                Admin admin = adminService.getById(userId);
                if (admin != null) {
                    principalUser = admin;
                    // 为管理员赋予所有权限的象征
                    permissions = List.of("ROLE_ADMIN", "user:read", "user:write");
                }
                break;
            case "CARE_WORKER":
                CareWorkerInfo careWorker = careWorkersSelfService.getById(userId); // 假设有这个方法
                if (careWorker != null) {
                    principalUser = careWorker;
                    permissions = List.of("ROLE_CARE_WORKER", "user:read");
                }
                break;
            case "ELDER":
                ElderInfo elder = elderSelfService.getById(userId); // 假设有这个方法
                if (elder != null) {
                    principalUser = elder;
                    permissions = List.of("ROLE_ELDER", "user:self:read");
                }
                break;
            default:
                // 未知用户类型，直接返回 null
                return null;
        }

        if (principalUser != null) {
            return new LoginUser(principalUser, permissions);
        }

        return null;
    }
}