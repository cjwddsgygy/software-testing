// 文件路径: backend/src/main/java/com/yusheng/controller/AccountController.java
package com.yusheng.controller;

import com.yusheng.pojo.Result;
import com.yusheng.service.AccountService; // 我们将创建一个新的 AccountService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    /**
     * ✅ 功能 1: 获取当前登录用户的个人信息
     * 前端通过 GET /api/account/me 调用
     * @param authentication Spring Security 自动注入的当前认证信息
     * @return 包含当前用户详细信息的 Result 对象
     */
    @GetMapping("/me")
    public Result<Object> getMyInfo(Authentication authentication) {
        // authentication.getPrincipal() 返回的就是我们在 JwtAuthenticationTokenFilter 中
        // 放入的、完整的 Admin, CareWorker, 或 Elder 对象。
        return Result.success(authentication.getPrincipal());
    }

    /**
     * ✅ 功能 2: 更新当前登录用户的个人信息
     * 前端通过 PUT /api/account/me 调用
     * @param authentication Spring Security 自动注入的当前认证信息
     * @param updates 一个包含要更新字段的 Map (例如: {"name": "新名字", "password": "新密码"})
     * @return 更新成功后的用户对象
     */
    @PutMapping("/me")
    public Result<Object> updateMyInfo(Authentication authentication, @RequestBody Map<String, Object> updates) {
        try {
            Object updatedUser = accountService.updateUserInfo(authentication, updates);
            return Result.success(updatedUser);
        } catch (RuntimeException e) {
            // 捕获 Service 层可能抛出的异常 (如“找不到用户”)，并返回给前端
            return Result.error(e.getMessage());
        }
    }
}