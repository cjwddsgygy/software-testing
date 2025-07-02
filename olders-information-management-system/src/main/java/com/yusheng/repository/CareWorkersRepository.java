// backend/src/main/java/com/yusheng/repository/CareWorkersRepository.java
package com.yusheng.repository; // 注意包名是 com.yusheng.repository

import com.yusheng.pojo.CareWorker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;     // ✅ 1. 导入 Page
import org.springframework.data.domain.Pageable; // ✅ 2. 导入 Pageable

import java.util.List;
import java.util.Optional;

@Repository
public interface CareWorkersRepository extends JpaRepository<CareWorker, Integer> {

    /**
     * ✅ 新增：根据账号精确查找护工
     * 这个方法是 UserDetailsServiceImpl 和登录逻辑所必需的。
     * @param account 账号
     * @return 一个包含 CareWorker 的 Optional 对象
     */
    Optional<CareWorker> findByAccount(String account);

    @Query("SELECT c FROM CareWorker c WHERE " +
            "(:searchTerm IS NULL OR :searchTerm = '') OR " +
            "(CAST(c.id AS string) LIKE %:searchTerm% OR c.name LIKE %:searchTerm%)")
    Page<CareWorker> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

}