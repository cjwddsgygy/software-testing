package com.yusheng.service.impl;

import com.yusheng.pojo.SystemSetting;
import com.yusheng.repository.SystemSettingRepository;
import com.yusheng.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    // 定义默认设置，用于“恢复出厂设置”功能
    private static final Map<String, String> DEFAULT_SETTINGS = Map.of(
            "systemName", "养老院信息管理系统",
            "copyright", "© {{year}} guangyu Technologies",
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

    /**
     * ✅✅✅ 核心修复：采用“先查后存”的策略来确保更新成功 ✅✅✅
     */
    @Override
    @Transactional
    public void saveSettings(Map<String, String> settingsMap) {
        settingsMap.forEach((key, value) -> {
            // 1. 根据 key (主键) 从数据库中查找现有的设置项
            Optional<SystemSetting> existingSettingOpt = systemSettingRepository.findById(key);

            SystemSetting settingToSave;
            if (existingSettingOpt.isPresent()) {
                // 2. 如果找到了，说明是更新操作
                settingToSave = existingSettingOpt.get();
                // 只更新 value 字段
                settingToSave.setValue(value);
            } else {
                // 3. 如果没找到，说明是新增操作
                settingToSave = new SystemSetting(key, value);
            }

            // 4. 调用 save 方法，此时 JPA 能明确地知道是更新还是新增
            systemSettingRepository.save(settingToSave);
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