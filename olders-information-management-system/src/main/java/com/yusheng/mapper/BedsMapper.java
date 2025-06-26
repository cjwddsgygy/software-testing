package com.yusheng.mapper;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Expense;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BedsMapper {

    //    查询全部床位情况
    List<Bed> findAll();

    //    删除床位
    void deleteById(Integer id);

    //     新增床位
    void save(Bed bed);

    //    根据ID查询床位信息
    Bed getById(Integer id);

    //    修改床位
    void updateById(Bed bed);
}
