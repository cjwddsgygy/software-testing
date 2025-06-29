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
    private SystemSettingService systemsettingService;

    /**
     * 获取所有系统设置
     * @return Result<Map<String, String>>
     */
    @GetMapping
    public Result<Map<String, String>> getSettings() {
        Map<String, String> settings = systemsettingService.getAllSettings();
        return Result.success(settings);
    }

    /**
     * 保存系统设置
     * @param settingsMap 前端发送的设置对象
     * @return Result<Void>
     */
    @PostMapping
    public Result<Void> saveSettings(@RequestBody Map<String, String> settingsMap) {
        systemsettingService.saveSettings(settingsMap);
        return Result.success();
    }

    /**
     * 恢复为默认设置
     * @return Result<Void>
     */
    @PostMapping("/reset")
    public Result<Void> resetSettings() {
        systemsettingService.resetToDefaults();
        return Result.success();
    }
}