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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        // 我们只对非登录请求进行处理
        if (request.getRequestURI().equals("/admin/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("\n\n<<<<<<<<<<<<<<<<<< JWT FILTER DEBUG START >>>>>>>>>>>>>>>>>>");
        System.out.println("REQUEST URI: " + request.getRequestURI());
        System.out.println("AUTHORIZATION HEADER: " + authHeader);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            System.out.println("  [Step 1] Extracted JWT: " + jwt);

            Claims claims = JwtUtils.parseToken(jwt);

            if (claims != null) {
                System.out.println("  [Step 2] SUCCESS: JWT parsing successful.");
                System.out.println("     Claims content: " + claims.toString());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("  [Step 3] INFO: SecurityContext is empty, proceeding with authentication.");

                    Integer adminId = claims.get("id", Integer.class);
                    String account = claims.get("account", String.class);

                    System.out.println("     [DEBUG] Claim 'id' value: " + adminId);
                    System.out.println("     [DEBUG] Claim 'account' value: " + account);

                    if (adminId != null && StringUtils.hasText(account)) {
                        UserDetails userDetails = new User(account, "", new ArrayList<>());
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("  [Step 4] SUCCESS: Authentication object set in SecurityContext for user: " + account);
                    } else {
                        System.out.println("  [Step 4] FAILURE: 'id' or 'account' claim is missing from token.");
                    }
                } else {
                    System.out.println("  [Step 3] INFO: SecurityContext already contains an Authentication object. Skipping.");
                }
            } else {
                System.out.println("  [Step 2] FAILURE: JWT parsing returned NULL. Token is likely invalid, expired, or signature does not match.");
            }
        } else {
            System.out.println("  [Step 1] INFO: No Bearer token found in request.");
        }

        System.out.println("<<<<<<<<<<<<<<<<<<< JWT FILTER DEBUG END >>>>>>>>>>>>>>>>>\n\n");

        filterChain.doFilter(request, response);
    }
}