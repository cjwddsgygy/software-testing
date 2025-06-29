package com.yusheng.service;

import com.yusheng.pojo.Expense;
import org.springframework.data.domain.Page;

public interface ExpensesService {

    // 查询费用数据（支持搜索和分页）
    Page<Expense> list(String search, int page, int pageSize);

    // 删除费用
    void deleteById(Integer id);

    // 新增费用
    void save(Expense expense);

    // 根据ID查询费用信息
    Expense getInfo(Integer id);

    // 修改费用
    void update(Expense expense);
}
