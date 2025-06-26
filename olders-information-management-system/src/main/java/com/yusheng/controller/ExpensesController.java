package com.yusheng.controller;

import com.yusheng.pojo.Expense;
import com.yusheng.pojo.Result;
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/expenses")
@RestController
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    //    查询全部费用数据
    @GetMapping
    public Result list() {
        List<Expense> expenseList = expensesService.findAll();
        return Result.success(expenseList);
    }

    //    删除费用
    @DeleteMapping
    public Result delete(Integer id) {
        expensesService.deleteById(id);
        return Result.success();
    }

    //    新增费用
    @PostMapping
    public Result save(@RequestBody Expense expense) {
        expensesService.save(expense);
        return Result.success();
    }

    //    根据ID查询费用信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Expense expense = expensesService.getInfo(id);
        return Result.success(expense);
    }

    //    修改费用
    @PutMapping
    public Result update(@RequestBody Expense expense) {
        expensesService.update(expense);
        return Result.success();
    }
}
