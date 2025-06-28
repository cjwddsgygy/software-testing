// backend/src/main/java/com/yusheng/repository/BedRepository.java
package com.yusheng.repository; // 注意包名是 com.yusheng.repository

import com.yusheng.pojo.Bed; // 导入正确路径的 Bed 实体
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> { // Bed实体的主键是Integer
    /**
     * 根据床位状态统计床位数量
     * @param status 床位状态，例如 "空闲", "已入住", "维护中"
     * @return 符合该状态的床位数量
     */
    long countByStatus(String status);
}