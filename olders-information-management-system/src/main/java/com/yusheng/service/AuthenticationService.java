// 文件路径: src/main/java/com/yusheng/service/AuthenticationService.java (最终重构版)
package com.yusheng.service;

import com.yusheng.dto.LoginResponseDto;
import com.yusheng.pojo.Admin;
import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Elder;
import com.yusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class AuthenticationService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CareWorkersService careWorkersService; // ✅ 确保 Service 命名统一
    @Autowired
    private ElderService elderService;           // ✅ 确保 Service 命名统一

    // ✅✅✅ 核心重构：将三个 login 方法，抽象成一个通用的私有方法 ✅✅✅
    private <T> LoginResponseDto performLogin(
            String account,
            String password,
            String role,
            BiFunction<String, String, T> loginFunction, // 一个接受账号密码、返回用户对象的方法引用
            Function<T, Integer> idExtractor,             // 一个从用户对象中提取 ID 的方法引用
            Function<T, String> accountExtractor          // 一个从用户对象中提取账号的方法引用
    ) {
        // 1. 调用传入的登录验证函数
        T authenticatedUser = loginFunction.apply(account, password);

        if (authenticatedUser != null) {
            // 2. 使用传入的提取器，安全地获取 ID 和账号
            Integer id = idExtractor.apply(authenticatedUser);
            String userAccount = accountExtractor.apply(authenticatedUser);

            // 3. 创建 JWT 载荷并生成 Token
            Map<String, Object> claims = createClaims(id, userAccount, role);
            String token = JwtUtils.generateToken(claims);

            // 4. 封装 DTO 并返回
            return new LoginResponseDto(token, role, authenticatedUser);
        }
        return null;
    }

    // --- 公开的登录方法现在变得极其简洁 ---

    public LoginResponseDto loginAsAdmin(String account, String password) {
        return performLogin(
                account,
                password,
                "admin",
                adminService::login, // 传入 AdminService 的 login 方法
                Admin::getId,        // 传入 Admin 类的 getId 方法引用
                Admin::getAccount    // 传入 Admin 类的 getAccount 方法引用
        );
    }

    public LoginResponseDto loginAsCareWorker(String account, String password) {
        return performLogin(
                account,
                password,
                "careworker",
                careWorkersService::login,
                CareWorker::getId,
                CareWorker::getAccount
        );
    }

    public LoginResponseDto loginAsElder(String account, String password) {
        return performLogin(
                account,
                password,
                "elder",
                elderService::login,
                Elder::getId,
                Elder::getAccount
        );
    }

    // 创建 JWT claims 的辅助方法 (保持不变)
    private Map<String, Object> createClaims(Integer id, String account, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("account", account);
        claims.put("role", role);
        return claims;
    }
}