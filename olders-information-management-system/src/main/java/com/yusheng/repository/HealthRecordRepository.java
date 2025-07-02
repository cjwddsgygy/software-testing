// 文件路径: src/main/java/com/yusheng/repository/HealthRecordRepository.java
package com.yusheng.repository;

import com.yusheng.pojo.HealthRecord; // ✅ 1. 确保导入了正确的 Pojo
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
// ✅✅✅ 2. 核心修复：泛型参数直接使用类名 HealthRecord ✅✅✅
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer>, JpaSpecificationExecutor<HealthRecord> {
    // 这里可以保持为空，因为所有需要的方法都已由父接口提供
}