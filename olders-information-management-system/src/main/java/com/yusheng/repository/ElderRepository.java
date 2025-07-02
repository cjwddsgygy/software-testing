// 文件路径: src/main/java/com/yusheng/repository/ElderRepository.java
package com.yusheng.repository;

import org.springframework.data.domain.Page;     // ✅ 1. 导入 Page
import org.springframework.data.domain.Pageable; // ✅ 2. 导入 Pageable
import com.yusheng.pojo.Elder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ✅ 导入 @Query
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElderRepository extends JpaRepository<Elder, Integer> {

    /**
     * 根据账号精确查找用户 (这个通常不需要分页)
     */
    Optional<Elder> findByAccount(String account);

    /**
     * 根据床位号精确查找老人 (这个通常也不需要分页)
     */
    Optional<Elder> findByBedNumber(String bedNumber);


    Page<Elder> findByNameContaining(String name, Pageable pageable);


    @Query("SELECT e FROM Elder e WHERE e.bedNumber IS NULL OR e.bedNumber = ''")
    Page<Elder> findUnassigned(Pageable pageable);

    Page<Elder> findByIdOrNameContaining(Integer id, String searchTerm, Pageable pageable);
}