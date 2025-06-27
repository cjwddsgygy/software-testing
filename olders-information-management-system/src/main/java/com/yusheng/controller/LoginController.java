package com.yusheng.controller;

import com.yusheng.pojo.*;
import com.yusheng.service.AdminService;
import com.yusheng.service.CareWorkersSelfService;
import com.yusheng.service.ElderSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//    管理员登录
    @PostMapping("/admin/login")
    public Result login(@RequestBody Admin admin) {
        AdminInfo adminInfo = adminService.login(admin);
        if (adminInfo != null) {
            return Result.success(adminInfo);
        }
        return Result.error("用户名或密码错误");
    }

//    护工登录
    @PostMapping("/careWorkerLogin")
    public Result login(@RequestBody CareWorker careWorker) {
        CareWorkerInfo careWorkerInfo = careWorkersSelfService.login(careWorker);
        if (careWorkerInfo != null) {
            return Result.success(careWorkerInfo);
        }
        return Result.error("用户名或密码错误");
    }

//    老人登录
    @PostMapping("/elderLogin")
    public Result login(@RequestBody Elder elder) {
        ElderInfo elderInfo = elderSelfService.login(elder);
        if (elderInfo != null) {
            return Result.success(elderInfo);
        }
        return Result.error("用户名或密码错误");
    }
}
