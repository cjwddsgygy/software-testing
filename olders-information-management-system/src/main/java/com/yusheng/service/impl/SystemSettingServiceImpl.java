package com.yusheng.service.impl;

import com.yusheng.pojo.SystemSetting;
import com.yusheng.repository.SystemSettingRepository;
import com.yusheng.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    // 定义默认设置，用于“恢复出厂设置”功能
    private static final Map<String, String> DEFAULT_SETTINGS = Map.of(
            "systemName", "智慧养老院管理系统",
            "copyright", "© {{year}} Yusheng Technologies",
            "theme", "light",
            "logo", "", // 默认没有 logo
            "favicon", "", // 默认没有 favicon
            "sessionTimeout", "30" // 默认30分钟
    );

    @Override
    public Map<String, String> getAllSettings() {
        List<SystemSetting> settingsList = systemSettingRepository.findAll();
        // 将 List<SystemSetting> 转换为 Map<String, String>
        Map<String, String> settingsMap = settingsList.stream()
                .collect(Collectors.toMap(SystemSetting::getKey, SystemSetting::getValue));

        // 确保所有默认键都存在，如果数据库中没有，则使用默认值填充
        DEFAULT_SETTINGS.forEach(settingsMap::putIfAbsent);

        return settingsMap;
    }

    @Override
    @Transactional // 保证所有设置项的保存是一个原子操作
    public void saveSettings(Map<String, String> settingsMap) {
        settingsMap.forEach((key, value) -> {
            // 对于每个键值对，创建一个新的 SystemSetting 对象并保存
            // JPA 的 save 方法会自动处理新增或更新逻辑
            SystemSetting setting = new SystemSetting(key, value);
            systemSettingRepository.save(setting);
        });
    }

    @Override
    @Transactional
    public void resetToDefaults() {
        // 先清空所有现有设置
        systemSettingRepository.deleteAll();
        // 然后将默认设置保存到数据库
        DEFAULT_SETTINGS.forEach((key, value) -> {
            systemSettingRepository.save(new SystemSetting(key, value));
        });
    }
}