package com.yusheng.controller;

import com.yusheng.pojo.Expense;
import com.yusheng.pojo.Result;
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/expenses")
@RestController
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    // GET /expenses - 查询列表（支持搜索和分页）
    @GetMapping
    public Result list(@RequestParam(required = false) String search,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize) {

        Page<Expense> expensePage = expensesService.list(search, page, pageSize);

        // 封装成前端需要的数据结构 { list: [...], total: ... }
        Map<String, Object> data = new HashMap<>();
        data.put("list", expensePage.getContent());
        data.put("total", expensePage.getTotalElements());

        return Result.success(data);
    }

    // POST /expenses - 新增费用
    @PostMapping
    public Result save(@RequestBody Expense expense) {
        expensesService.save(expense);
        return Result.success();
    }

    // GET /expenses/{id} - 根据ID查询详情
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Expense expense = expensesService.getInfo(id);
        if (expense == null) {
            return Result.error("记录不存在");
        }
        return Result.success(expense);
    }

    // PUT /expenses/{id} - 修改费用 (RESTful 风格)
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Expense expense) {
        expense.setId(id); // 确保 ID 正确
        expensesService.update(expense);
        return Result.success();
    }

    // DELETE /expenses/{id} - 删除费用 (RESTful 风格)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        expensesService.deleteById(id);
        return Result.success();
    }
}
