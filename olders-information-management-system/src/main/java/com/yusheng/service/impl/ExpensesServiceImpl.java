package com.yusheng.service.impl;

import com.yusheng.pojo.Expense;
import com.yusheng.repository.ExpenseRepository; // 引入 JPA Repository
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    private ExpenseRepository expenseRepository; // 注入 JPA Repository

    @Override
    public Page<Expense> list(String search, int page, int pageSize) {
        // 创建分页请求，按 ID 降序排序，最新的在前面
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        if (StringUtils.hasText(search)) {
            // 如果有搜索词，调用自定义的搜索方法
            return expenseRepository.findByElderContainingOrItemNameContaining(search, search, pageable);
        } else {
            // 如果没有搜索词，查询所有
            return expenseRepository.findAll(pageable);
        }
    }

    @Override
    public void deleteById(Integer id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public void save(Expense expense) {
        // 新增时，JPA @PrePersist 注解会自动设置 createdAt 和 updatedAt
        // 默认状态可以设置为“未支付”
        if(expense.getStatus() == null) {
            expense.setStatus("未支付");
        }
        expenseRepository.save(expense);
    }

    @Override
    public Expense getInfo(Integer id) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        // 如果找不到，可以返回 null 或抛出异常
        return optionalExpense.orElse(null);
    }

    @Override
    public void update(Expense expense) {
        // 更新时，JPA @PreUpdate 注解会自动更新 updatedAt
        expenseRepository.save(expense); // save 方法在有 id 时是更新，没有 id 时是新增
    }
}
