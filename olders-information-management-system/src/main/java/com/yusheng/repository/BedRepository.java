package com.yusheng.repository;

import com.yusheng.pojo.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

    /**
     * 根据状态统计床位数量 (提供给 StatisticService 使用)
     * @param status 床位状态
     * @return 数量
     */
    long countByStatus(String status);

    /**
     * 自定义查询，用于搜索床位号或老人姓名
     * 使用JPQL (Java Persistence Query Language)
     * @param searchTerm 搜索关键字
     * @return 符合条件的床位列表
     */
    @Query("SELECT b FROM Bed b WHERE b.bedNumber LIKE %?1% OR b.name LIKE %?1%")
    List<Bed> findBySearchTerm(String searchTerm);
}
