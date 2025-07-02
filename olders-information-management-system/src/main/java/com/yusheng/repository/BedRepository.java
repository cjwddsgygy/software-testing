// 文件路径: src/main/java/com/yusheng/repository/BedRepository.java
package com.yusheng.repository;

import com.yusheng.pojo.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

    /**
     * 根据房间号或床位号进行模糊查询
     */
    List<Bed> findByRoomNumberContainingOrBedNumberContaining(String roomNumber, String bedNumber);

    /**
     * ✅✅✅ 核心修复：将参数类型从 Bed.BedStatus 改为 String ✅✅✅
     * Spring Data JPA 会自动生成 "WHERE status = ?" 的 SQL。
     * @param status 要查询的状态字符串，例如 "空闲" 或 "占用"
     * @return 对应状态的床位数量
     */
    long countByStatus(String status);
}