package com.yusheng.service.impl;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.pojo.PageResult;
import com.yusheng.repository.HealthRecordRepository;
import com.yusheng.service.HealthRecordsService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HealthRecordsServiceImpl implements HealthRecordsService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Override
    public PageResult<HealthRecord> findPage(String searchTerm, Pageable pageable) {
        // 使用 Specification 构建动态查询条件
        Specification<HealthRecord> spec = (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(searchTerm)) {
                // 创建一个模糊查询，匹配老人姓名 (elderName) 或记录类型 (recordType)
                Predicate elderNameLike = criteriaBuilder.like(root.get("elderName"), "%" + searchTerm + "%");
                Predicate recordTypeLike = criteriaBuilder.like(root.get("recordType"), "%" + searchTerm + "%");
                // 将两个条件用 OR 连接
                return criteriaBuilder.or(elderNameLike, recordTypeLike);
            }
            // 如果没有搜索词，返回 null，表示没有查询条件
            return null;
        };

        Page<HealthRecord> page = healthRecordRepository.findAll(spec, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements());
    }

    @Override
    public HealthRecord save(HealthRecord healthRecord) {
        // JPA的save方法会自动判断是新增还是更新 (根据ID是否存在)
        return healthRecordRepository.save(healthRecord);
    }

    @Override
    public HealthRecord findById(Integer id) {
        // orElse(null) 表示如果找不到就返回 null
        return healthRecordRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        healthRecordRepository.deleteById(id);
    }
}