package com.yusheng.repository;

import com.yusheng.pojo.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer>, JpaSpecificationExecutor<HealthRecord> {
    // JpaRepository 提供了所有基础的 CRUD 方法 (save, findById, deleteById, findAll, etc.)
    // JpaSpecificationExecutor 提供了使用 Specification 进行动态条件查询的能力，非常适合实现复杂的搜索功能。
}