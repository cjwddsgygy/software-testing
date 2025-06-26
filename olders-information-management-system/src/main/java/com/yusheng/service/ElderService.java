package com.yusheng.service;

import com.yusheng.pojo.Elder;

import java.util.List;

public interface ElderService {

    //    查询全部老人数据
    List<Elder> findAll();

    //    删除老人
    void deleteById(Integer id);

    //    新增老人
    void save(Elder elder);

    //    根据ID查询老人信息
    Elder getInfo(Integer id);

    //    修改老人
    void update(Elder elder);
}
