package com.yusheng.service;

import java.util.Map;

public interface SystemSettingService {

    /**
     * 获取所有系统设置，并转换为 Map<String, String> 格式
     * @return 包含所有设置的 Map
     */
    Map<String, String> getAllSettings();

    /**
     * 保存或更新设置
     * @param settingsMap 前端传来的设置 Map
     */
    void saveSettings(Map<String, String> settingsMap);

    /**
     * 恢复为默认设置
     */
    void resetToDefaults();
}