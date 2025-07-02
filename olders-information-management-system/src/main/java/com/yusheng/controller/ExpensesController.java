// 文件路径: backend/src/main/java/com/yusheng/controller/ExpensesController.java
package com.yusheng.controller;

import com.yusheng.pojo.Expense;
import com.yusheng.pojo.PageResult;
import com.yusheng.pojo.Result;
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @GetMapping
    public Result<PageResult<Expense>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        // 创建 Pageable 对象
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        // ✅✅✅ 核心修复 1：调用新的 findPage 方法 ✅✅✅
        Page<Expense> expensePage = expensesService.findPage(search, pageable);

        // 将 Spring Data Page 转换为我们自定义的 PageResult
        PageResult<Expense> result = new PageResult<>(
                expensePage.getContent(),
                expensePage.getTotalElements()
        );

        return Result.success(result);
    }

    @PostMapping
    public Result<Expense> save(@RequestBody Expense expense) {
        Expense savedExpense = expensesService.save(expense);
        return Result.success(savedExpense);
    }

    @GetMapping("/{id}")
    public Result<Expense> getById(@PathVariable Integer id) { // ✅ 方法名改为 getById
        // ✅✅✅ 核心修复 2：调用新的 findById 方法 ✅✅✅
        Expense expense = expensesService.findById(id);
        if (expense == null) {
            return Result.error("记录不存在");
        }
        return Result.success(expense);
    }

    @PutMapping("/{id}")
    public Result<Expense> update(@PathVariable Integer id, @RequestBody Expense expense) {
        expense.setId(id);
        Expense updatedExpense = expensesService.update(expense);
        return Result.success(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        expensesService.deleteById(id);
        return Result.success();
    }
}
