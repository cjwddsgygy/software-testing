// 文件路径: src/main/java/com/yusheng/repository/ExpenseRepository.java
package com.yusheng.repository;

import com.yusheng.pojo.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    /**
     * ✅ 确保有这个方法来支持搜索功能
     * 根据老人姓名或项目名称进行模糊查询，并支持分页
     */
    Page<Expense> findByElderContainingOrItemNameContaining(String elder, String itemName, Pageable pageable);

    /**
     * ✅ 这是我们之前为首页统计添加的方法
     */
    @Query(
            value = "SELECT COUNT(*) FROM expenses WHERE DATE_FORMAT(expense_date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')",
            nativeQuery = true
    )
    long countExpensesThisMonth();
}