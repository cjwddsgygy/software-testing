package com.yusheng.service;

import com.yusheng.pojo.Bed;

import java.util.List;

public interface BedsService {

    //    查询全部床位情况
    List<Bed> findAll();

    //    删除床位
    void deleteById(Integer id);

    //    新增床位
    void save(Bed bed);

    //    根据ID查询床位信息
    Bed getInfo(Integer id);

    //    修改床位
    void update(Bed bed);
}
