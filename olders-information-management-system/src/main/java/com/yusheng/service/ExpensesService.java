package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Expense;

import java.util.List;

public interface ExpensesService {

    //    查询全部费用数据
    List<Expense> findAll();

    //    删除费用
    void deleteById(Integer id);

    //    新增费用
    void save(Expense expense);

    //    根据ID查询费用信息
    Expense getInfo(Integer id);

    //    修改费用
    void update(Expense expense);
}
