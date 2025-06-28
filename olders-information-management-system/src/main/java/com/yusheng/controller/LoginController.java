// 文件路径: backend/src/main/java/com/yusheng/controller/LoginController.java
package com.yusheng.controller;

// ... (imports 保持不变, 确保导入了 Map, HashMap, JwtUtils)
import com.yusheng.pojo.*;
import com.yusheng.service.AdminService;
import com.yusheng.service.CareWorkersSelfService;
import com.yusheng.service.ElderSelfService;
import com.yusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CareWorkersSelfService careWorkersSelfService;
    @Autowired
    private ElderSelfService elderSelfService;

    @PostMapping("/api/adminLogin")
    public Result<String> login(@RequestBody Admin loginAdmin) {
        Admin authenticatedAdmin = adminService.login(loginAdmin.getAccount(), loginAdmin.getPassword());
        if (authenticatedAdmin != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", authenticatedAdmin.getId());
            claims.put("account", authenticatedAdmin.getAccount());
            // 关键修改: 明确添加用户类型
            claims.put("userType", "ADMIN");
            String token = JwtUtils.generateToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/api/careWorkerLogin")
    public Result<String> login(@RequestBody CareWorker careWorker) {
        CareWorkerInfo careWorkerInfo = careWorkersSelfService.login(careWorker);
        if (careWorkerInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", careWorkerInfo.getId());
            // 关键修改: 明确添加用户类型
            claims.put("userType", "CARE_WORKER");
            String token = JwtUtils.generateToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/api/elderLogin")
    public Result<String> login(@RequestBody Elder elder) {
        ElderInfo elderInfo = elderSelfService.login(elder);
        if (elderInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", elderInfo.getId());
            claims.put("account", elderInfo.getAccount());
            // 关键修改: 明确添加用户类型
            claims.put("userType", "ELDER");
            String token = JwtUtils.generateToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }
}