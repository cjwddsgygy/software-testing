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

    @Override
    public Map<String, String> getAllSettings() {
        // 从数据库查询所有设置，并转换为 Map<Key, Value> 的形式
        return systemSettingRepository.findAll().stream()
                .collect(Collectors.toMap(SystemSetting::getKey, SystemSetting::getValue));
    }

    @Override
    @Transactional // 保证所有设置在一个事务中被保存
    public void saveAllSettings(Map<String, String> settings) {
        settings.forEach((key, value) -> {
            SystemSetting setting = new SystemSetting(key, value);
            // JPA的save方法：如果主键已存在，则更新；如果不存在，则插入。
            systemSettingRepository.save(setting);
        });
    }
}