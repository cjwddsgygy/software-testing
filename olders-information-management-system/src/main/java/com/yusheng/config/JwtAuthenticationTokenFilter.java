// 文件路径: backend/src/main/java/com/yusheng/config/JwtAuthenticationTokenFilter.java
package com.yusheng.config;

import com.yusheng.pojo.Admin;
import com.yusheng.pojo.CareWorker; // 确保导入
import com.yusheng.pojo.Elder;    // 确保导入
import com.yusheng.service.AdminService;
import com.yusheng.service.CareWorkersSelfService;
import com.yusheng.service.ElderSelfService;
import com.yusheng.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Claims claims = JwtUtils.parseToken(jwt);

            // 如果Token有效，并且当前安全上下文中没有认证信息
            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Integer userId = claims.get("id", Integer.class);
                String userType = claims.get("role", String.class); // 从Token中获取角色

                if (userId != null && StringUtils.hasText(userType)) {

                    // ✅✅✅ 核心修改：不再使用 UserDetails，直接构建 AuthenticationToken ✅✅✅
                    Object userPrincipal = null; // 用来存放 Admin, CareWorker 或 Elder 对象
                    List<GrantedAuthority> authorities = Collections.emptyList();

                    // 根据角色，从数据库加载对应的完整用户对象
                    switch (userType.toUpperCase()) { // 转换为大写，增加健壮性
                        case "ADMIN":
                            userPrincipal = adminService.getById(userId);
                            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
                            break;
                        case "CAREWORKER":
                            // 注意：这里需要一个能返回完整 CareWorker 的方法
                            userPrincipal = careWorkersSelfService.getInfo(userId);
                            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CAREWORKER"));
                            break;
                        case "ELDER":
                            // 注意：这里需要一个能返回完整 Elder 的方法
                            userPrincipal = elderSelfService.getInfo(userId);
                            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ELDER"));
                            break;
                    }

                    // 如果成功加载到用户信息
                    if (userPrincipal != null) {
                        // 直接用加载到的用户对象作为 principal 创建认证令牌
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userPrincipal, // ✅ 直接使用 Admin, CareWorker 或 Elder 对象
                                        null,
                                        authorities  // ✅ 传入角色权限
                                );

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 将认证信息放入安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("User authenticated: " + claims.get("account") + " with role: " + userType);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}