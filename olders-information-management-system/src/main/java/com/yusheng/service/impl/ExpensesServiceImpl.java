package com.yusheng.service.impl;

import com.yusheng.mapper.ExpensesMapper;
import com.yusheng.pojo.Expense;
import com.yusheng.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    private ExpensesMapper expensesMapper;

    //    查询全部费用数据
    @Override
    public List<Expense> findAll() {
        return expensesMapper.findAll();
    }

    //    删除费用
    @Override
    public void deleteById(Integer id) {
        expensesMapper.deleteById(id);
    }

    //    新增费用
    @Override
    public void save(Expense expense) {
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());
        expensesMapper.save(expense);
    }

    //    根据ID查询费用信息
    @Override
    public Expense getInfo(Integer id) {
        return expensesMapper.getById(id);
    }

    //    修改费用
    @Override
    public void update(Expense expense) {
        expense.setUpdatedAt(LocalDateTime.now());
        expensesMapper.updateById(expense);
    }

}
