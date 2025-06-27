// backend/src/main/java/com/yusheng/config/JwtAuthenticationTokenFilter.java
package com.yusheng.config;

import com.yusheng.pojo.Admin;
import com.yusheng.service.AdminService;
import com.yusheng.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// --- 核心修改在这里：导入 jakarta 包 ---
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头获取 token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 提取 token (去掉 "Bearer " 前缀)
        token = token.substring(7);

        // 3. 解析 token, 获取用户ID
        Integer adminId;
        try {
            Claims claims = JwtUtils.parseToken(token);
            adminId = (Integer) claims.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法或已过期");
        }

        // 4. 从数据库查询管理员信息
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            throw new RuntimeException("用户不存在");
        }

        // 5. 创建 Spring Security 需要的 UserDetails 对象
        UserDetails userDetails = new User(admin.getAccount(), "", new ArrayList<>());

        // 6. 创建认证信息对象，并存入 SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 7. 放行
        filterChain.doFilter(request, response);
    }
}