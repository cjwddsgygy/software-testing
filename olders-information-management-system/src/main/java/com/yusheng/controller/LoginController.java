package com.yusheng.controller;

import com.yusheng.pojo.*;
import com.yusheng.service.AdminService;
import com.yusheng.service.CareWorkersSelfService;
import com.yusheng.service.ElderSelfService;
import com.yusheng.utils.JwtUtils; // ✅ 1. 导入 JwtUtils
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap; // ✅ 2. 导入 HashMap
import java.util.Map;   // ✅ 3. 导入 Map

/**
 * 登录Controller
 */
@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CareWorkersSelfService careWorkersSelfService;

    @Autowired
    private ElderSelfService elderSelfService;

    // 管理员登录
    @PostMapping("/admin/login")
    public Result<String> login(@RequestBody Admin loginAdmin) { // ✅ 参数名修改为 loginAdmin，返回值仍为 Result<String>
        // 1. 调用 AdminService 的 login 方法验证管理员
        // AdminService.login 现在返回 Admin 对象，或者 null
        Admin authenticatedAdmin = adminService.login(loginAdmin.getAccount(), loginAdmin.getPassword());

        // 2. 判断是否认证成功
        if (authenticatedAdmin != null) {
            // 3. 认证成功，生成 JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", authenticatedAdmin.getId()); // 从 Admin 对象中获取 ID
            claims.put("account", authenticatedAdmin.getAccount()); // 从 Admin 对象中获取 Account
            // 你还可以添加角色信息，以便后续进行更细粒度的权限控制
            // claims.put("role", "ADMIN");

            String token = JwtUtils.generateToken(claims);

            // 4. 将 Token 字符串作为成功结果返回
            return Result.success(token);
        }

        // 5. 不存在或密码错误，返回错误信息
        return Result.error("用户名或密码错误");
    }

    // 护工登录 (同样需要修改)
    @PostMapping("/careWorkerLogin")
    public Result<String> login(@RequestBody CareWorker careWorker) {
        CareWorkerInfo careWorkerInfo = careWorkersSelfService.login(careWorker);
        if (careWorkerInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", careWorkerInfo.getId());
            //    claims.put("account", careWorkerInfo.getAccount()); // 假设护工也有账号
            claims.put("role", "CARE_WORKER");

            String token = JwtUtils.generateToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }

    // 老人登录 (同样需要修改)
    @PostMapping("/elderLogin")
    public Result<String> login(@RequestBody Elder elder) {
        ElderInfo elderInfo = elderSelfService.login(elder);
        if (elderInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", elderInfo.getId());
            claims.put("account", elderInfo.getAccount());
            claims.put("role", "ELDER");

            String token = JwtUtils.generateToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }
}