// 文件路径: backend/src/main/java/com/yusheng/controller/LoginController.java
package com.yusheng.controller;

import com.yusheng.dto.LoginRequestDto;
import com.yusheng.dto.LoginResponseDto;
import com.yusheng.pojo.Result;
import com.yusheng.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public Result<LoginResponseDto> unifiedLogin(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto responseData = null;

        // 确保 role 不为 null，避免 NullPointerException
        String role = loginRequest.getRole() != null ? loginRequest.getRole().toLowerCase() : "";

        switch (role) {
            case "admin":
                responseData = authenticationService.loginAsAdmin(loginRequest.getAccount(), loginRequest.getPassword());
                break;
            case "careworker":
                responseData = authenticationService.loginAsCareWorker(loginRequest.getAccount(), loginRequest.getPassword());
                break;
            case "elder":
                responseData = authenticationService.loginAsElder(loginRequest.getAccount(), loginRequest.getPassword());
                break;
            default:
                return Result.error("无效的用户角色");
        }

        if (responseData != null) {
            return Result.success(responseData);
        }

        return Result.error("用户名或密码错误");
    }
}