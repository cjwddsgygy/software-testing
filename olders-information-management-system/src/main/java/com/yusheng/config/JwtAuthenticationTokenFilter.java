// 文件路径: backend/src/main/java/com/yusheng/config/JwtAuthenticationTokenFilter.java
package com.yusheng.config;

import com.yusheng.pojo.Admin;
import com.yusheng.service.AdminService;
import com.yusheng.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            // 调用我们健壮的 parseToken 方法，它可能返回 null
            Claims claims = JwtUtils.parseToken(jwt);

            // ✅✅✅ 最终、最关键的修改：在访问 claims 对象之前，必须检查它是否为 null！
            if (claims != null) {
                // 只有当 claims 不是 null 时，才尝试从中获取 id
                Integer adminId = (Integer) claims.get("id");

                if (adminId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Admin admin = adminService.getById(adminId);
                    if (admin != null) {
                        UserDetails userDetails = new User(admin.getAccount(), "", new ArrayList<>());
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            // 如果 claims 是 null，我们会直接跳过整个 if 代码块，不执行任何操作
        }

        // 无论如何，最后都必须放行请求
        filterChain.doFilter(request, response);
    }
}