package com.yusheng.mapper;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Expense;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExpensesMapper {

    //    查询全部费用数据
    List<Expense> findAll();

    //    删除费用
    void deleteById(Integer id);

    //     新增费用
    void save(Expense expense);

    //    根据ID查询费用信息
    Expense getById(Integer id);

    //    修改费用
    void updateById(Expense expense);
}
