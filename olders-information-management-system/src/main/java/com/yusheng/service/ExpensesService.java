// 文件路径: src/main/java/com/yusheng/service/ExpensesService.java
package com.yusheng.service;

import com.yusheng.pojo.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpensesService {

    /**
     * ✅✅✅ 核心修复 1：统一方法签名 ✅✅✅
     * 根据条件分页查询消费记录
     * @param searchTerm 搜索关键词
     * @param pageable   分页参数对象
     * @return Spring Data 的 Page 对象
     */
    Page<Expense> findPage(String searchTerm, Pageable pageable);

    /**
     * 新增一条消费记录
     * @return 保存后的对象
     */
    Expense save(Expense expense);

    /**
     * 更新一条消费记录
     * @return 更新后的对象
     */
    Expense update(Expense expense);

    /**
     * ✅✅✅ 核心修复 2：将 getById 改为 findById ✅✅✅
     * 根据ID查询消费记录
     * @return Expense 对象或 null
     */
    Expense findById(Integer id);

    /**
     * 根据ID删除消费记录
     */
    void deleteById(Integer id);
}