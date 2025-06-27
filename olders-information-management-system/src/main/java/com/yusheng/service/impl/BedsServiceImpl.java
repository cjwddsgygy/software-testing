package com.yusheng.service.impl;

import com.yusheng.mapper.BedsMapper;
import com.yusheng.pojo.Bed;
import com.yusheng.service.BedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BedsServiceImpl implements BedsService {

    @Autowired
    public BedsMapper bedsMapper;

    //    查询全部床位情况
    @Override
    public List<Bed> findAll() {
        return bedsMapper.findAll();
    }

    //    删除床位
    @Override
    public void deleteById(Integer id) {
        bedsMapper.deleteById(id);
    }

    //    新增床位
    @Override
    public void save(Bed bed) {
        bed.setCreatedAt(LocalDateTime.now());
        bed.setUpdatedAt(LocalDateTime.now());
        bedsMapper.save(bed);
    }

    //    根据ID查询床位信息
    @Override
    public Bed getInfo(Integer id) {
        return bedsMapper.getById(id);
    }

    //    修改床位
    @Override
    public void update(Bed bed) {
        bed.setUpdatedAt(LocalDateTime.now());
        bedsMapper.updateById(bed);
    }



    @Override
    public List<Bed> findPage(String status, String search) {
        return bedsMapper.findPage();
    }

}


