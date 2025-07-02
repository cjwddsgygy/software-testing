// 文件路径: src/main/java/com/yusheng/service/HealthRecordsService.java
package com.yusheng.service;

import com.yusheng.pojo.HealthRecord; // ✅ 使用正确的 HealthRecord 类型
import org.springframework.data.domain.Page;    // ✅ 导入并使用 Page
import org.springframework.data.domain.Pageable;

public interface HealthRecordsService {

    /**
     * 根据条件分页查询健康记录
     * @return Spring Data 的 Page 对象，包含了分页信息和数据列表
     */
    Page<HealthRecord> findPage(String searchTerm, Pageable pageable);

    /**
     * 新增或更新健康记录
     * @return 保存后的对象
     */
    HealthRecord save(HealthRecord healthRecord);

    /**
     * 更新健康记录（提供一个语义更明确的方法）
     */
    HealthRecord update(HealthRecord healthRecord);

    /**
     * 根据ID查询健康记录
     */
    HealthRecord findById(Integer id);

    /**
     * 根据ID删除健康记录
     */
    void deleteById(Integer id);
}