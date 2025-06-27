package com.yusheng.mapper;

import com.yusheng.pojo.Elder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ElderMapper {

    //    查询全部老人数据
    List<Elder> findAll(String name);

    //    查询无床位老人数据
    List<Elder> findUnassigned();

    //    删除老人
    void deleteById(Integer id);

    //     新增老人
    void save(Elder elder);

    //    根据ID查询老人信息
    Elder getById(Integer id);

    //    修改老人
    void updateById(Elder elder);

}
