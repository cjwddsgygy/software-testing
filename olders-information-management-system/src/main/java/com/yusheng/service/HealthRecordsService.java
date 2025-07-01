package com.yusheng.service;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.pojo.PageResult;
import org.springframework.data.domain.Pageable;

public interface HealthRecordsService {

    /**
     * 根据条件分页查询健康记录
     *
     * @param searchTerm 搜索关键词 (老人姓名或记录类型)
     * @param pageable   分页参数
     * @return 分页结果
     */
    PageResult<HealthRecord> findPage(String searchTerm, Pageable pageable);

    /**
     * 新增或更新健康记录
     * @param healthRecord 健康记录对象
     * @return 保存后的对象
     */
    HealthRecord save(HealthRecord healthRecord);

    /**
     * 根据ID查询健康记录
     * @param id 记录ID
     * @return HealthRecord 或 null
     */
    HealthRecord findById(Integer id);

    /**
     * 根据ID删除健康记录
     * @param id 记录ID
     */
    void deleteById(Integer id);
}
