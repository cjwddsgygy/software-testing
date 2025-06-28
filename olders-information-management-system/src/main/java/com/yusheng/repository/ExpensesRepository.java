
package com.yusheng.repository;

import com.yusheng.pojo.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 标记为一个 Spring Bean
public interface ExpensesRepository extends JpaRepository<Expense, Integer> { // Expenses 的主键是 Integer 类型
    // JpaRepository 提供了基本的 CRUD 操作，包括 count() 方法
}
