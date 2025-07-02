// 文件路径: src/main/java/com/yusheng/service/impl/HealthRecordsServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.repository.HealthRecordRepository;
import com.yusheng.service.HealthRecordsService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthRecordsServiceImpl implements HealthRecordsService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<HealthRecord> findPage(String searchTerm, Pageable pageable) {
        // 使用 Specification 构建动态查询条件
        Specification<HealthRecord> spec = (root, query, criteriaBuilder) -> {

            // 如果搜索词为空，直接返回 null，表示查询所有
            if (!StringUtils.hasText(searchTerm)) {
                return null;
            }

            // 1. 创建一个 Predicate 列表，用于存放所有可能的查询条件
            List<Predicate> predicates = new ArrayList<>();

            // 2. 条件一：按记录类型 (recordType) 进行模糊查询
            predicates.add(criteriaBuilder.like(root.get("recordType"), "%" + searchTerm + "%"));

            // 3. 条件二：按老人姓名 (elderName) 进行模糊查询
            predicates.add(criteriaBuilder.like(root.get("elderName"), "%" + searchTerm + "%"));

            // 4. 条件三：尝试将搜索词解析为 ID，如果成功，则添加 ID 精确查询
            try {
                Integer id = Integer.parseInt(searchTerm);
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // 如果 searchTerm 不是数字，就忽略这个条件，这没关系
            }

            // 5. 将列表中的所有条件用 OR 连接起来
            // toArray(new Predicate[0]) 是将 List<Predicate> 转换为 Predicate[] 的标准写法
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        return healthRecordRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public HealthRecord save(HealthRecord healthRecord) {
        // save 方法用于新增
        // Pojo 里的 @CreationTimestamp 会自动设置创建时间
        return healthRecordRepository.save(healthRecord);
    }

    @Override
    @Transactional
    public HealthRecord update(HealthRecord healthRecord) {
        // save 方法在有 ID 时会自动执行更新
        // Pojo 里的 @UpdateTimestamp 会自动更新时间
        if (healthRecord.getId() == null || !healthRecordRepository.existsById(healthRecord.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + healthRecord.getId() + " 的健康记录。");
        }
        return healthRecordRepository.save(healthRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public HealthRecord findById(Integer id) {
        return healthRecordRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        healthRecordRepository.deleteById(id);
    }
}