package com.yusheng.service;

import java.util.Map;

public interface SystemSettingService {
    // 获取所有设置项
    Map<String, String> getAllSettings();
    // 保存所有设置项
    void saveAllSettings(Map<String, String> settings);
}
