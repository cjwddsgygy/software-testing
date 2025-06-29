package com.yusheng.repository;

import com.yusheng.pojo.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {
    // JpaRepository 已经提供了所有我们需要的基础方法，如 findById, save, findAll 等。
    // 主键是 String 类型，所以这里是 <SystemSetting, String>
}