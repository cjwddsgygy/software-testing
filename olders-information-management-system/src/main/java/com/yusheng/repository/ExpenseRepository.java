package com.yusheng.repository; // 建议放在一个新的 repository 包下

import com.yusheng.pojo.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    /**
     * 根据老人姓名或项目名称进行模糊查询，并支持分页
     * Spring Data JPA 会根据方法名自动生成 SQL 查询！
     * "Containing" 关键字表示模糊匹配 (LIKE %...%)
     * "Or" 表示 OR 条件
     *
     * @param elderName 老人姓名
     * @param itemName  项目名称
     * @param pageable  分页参数 (包含页码、每页大小、排序信息)
     * @return 分页后的消费记录
     */
    Page<Expense> findByElderContainingOrItemNameContaining(String elderName, String itemName, Pageable pageable);

    /**
     * 当没有搜索词时，查询所有数据并分页
     *
     * @param pageable 分页参数
     * @return 分页后的所有消费记录
     */
    Page<Expense> findAll(Pageable pageable);
}