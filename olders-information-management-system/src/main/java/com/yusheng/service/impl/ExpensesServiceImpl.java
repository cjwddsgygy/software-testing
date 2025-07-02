// 文件路径: src/main/java/com/yusheng/service/impl/ExpensesServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.Expense;
import com.yusheng.repository.ExpenseRepository;
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * ✅✅✅ 核心修复 1：实现接口中定义的 findPage 方法 ✅✅✅
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Expense> findPage(String searchTerm, Pageable pageable) {
        if (StringUtils.hasText(searchTerm)) {
            return expenseRepository.findByElderContainingOrItemNameContaining(searchTerm, searchTerm, pageable);
        } else {
            return expenseRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    /**
     * ✅✅✅ 核心修复 2：实现接口中定义的 findById 方法 ✅✅✅
     */
    @Override
    @Transactional(readOnly = true)
    public Expense findById(Integer id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Expense update(Expense expense) {
        if (expense.getId() == null || !expenseRepository.existsById(expense.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + expense.getId() + " 的消费记录。");
        }
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        expenseRepository.deleteById(id);
    }
}