// 文件路径: src/main/java/com/yusheng/service/AuthenticationService.java
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

@Service
public class AuthenticationService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CareWorkersSelfService careWorkersSelfService;
    @Autowired
    private ElderSelfService elderSelfService;

    public LoginResponseDto loginAsAdmin(String account, String password) {
        Admin authenticatedAdmin = adminService.login(account, password);
        if (authenticatedAdmin != null) {
            String role = "admin";
            Map<String, Object> claims = createClaims(authenticatedAdmin.getId(), authenticatedAdmin.getAccount(), role);
            String token = JwtUtils.generateToken(claims);
            return new LoginResponseDto(token, role, authenticatedAdmin);
        }
        return null;
    }

    public LoginResponseDto loginAsCareWorker(String account, String password) {
        CareWorker request = new CareWorker();
        request.setAccount(account);
        request.setPassword(password);
        CareWorker authenticatedCareWorker = careWorkersSelfService.findFullCareWorkerByCredentials(request);
        if (authenticatedCareWorker != null) {
            String role = "careworker";
            Map<String, Object> claims = createClaims(authenticatedCareWorker.getId(), authenticatedCareWorker.getAccount(), role);
            String token = JwtUtils.generateToken(claims);
            return new LoginResponseDto(token, role, authenticatedCareWorker);
        }
        return null;
    }

    public LoginResponseDto loginAsElder(String account, String password) {
        Elder request = new Elder();
        request.setAccount(account);
        request.setPassword(password);
        Elder authenticatedElder = elderSelfService.findFullElderByCredentials(request);
        if (authenticatedElder != null) {
            String role = "elder";
            Map<String, Object> claims = createClaims(authenticatedElder.getId(), authenticatedElder.getAccount(), role);
            String token = JwtUtils.generateToken(claims);
            return new LoginResponseDto(token, role, authenticatedElder);
        }
        return null;
    }

    private Map<String, Object> createClaims(Integer id, String account, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("account", account);
        claims.put("role", role);
        return claims;
    }
}