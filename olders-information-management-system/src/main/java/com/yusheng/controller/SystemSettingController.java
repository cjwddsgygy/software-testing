package com.yusheng.controller;

import com.yusheng.pojo.Result;
import com.yusheng.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;

    // GET /settings - 获取所有系统设置
    @GetMapping
    public Result getAllSettings() {
        Map<String, String> settings = systemSettingService.getAllSettings();
        return Result.success(settings);
    }

    // POST /settings - 保存系统设置
    @PostMapping
    public Result saveAllSettings(@RequestBody Map<String, String> settings) {
        systemSettingService.saveAllSettings(settings);
        return Result.success();
    }
}