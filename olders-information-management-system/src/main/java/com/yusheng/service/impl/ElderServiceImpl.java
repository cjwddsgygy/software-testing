package com.yusheng.service.impl;

import com.yusheng.mapper.ElderMapper;
import com.yusheng.pojo.Elder;
import com.yusheng.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElderServiceImpl implements ElderService {

    @Autowired
    public ElderMapper elderMapper;

//    查询全部老人数据
    @Override
    public List<Elder> findAll() {
        return elderMapper.findAll();
    }

//    删除老人
    @Override
    public void deleteById(Integer id) {
        elderMapper.deleteById(id);
    }

//    新增老人
    @Override
    public void save(Elder elder) {
        elder.setCreatedAt(LocalDateTime.now());
        elder.setUpdatedAt(LocalDateTime.now());
        elderMapper.save(elder);
    }

//    根据ID查询老人信息
    @Override
    public Elder getInfo(Integer id) {
        return elderMapper.getById(id);
    }

//    修改老人
    @Override
    public void update(Elder elder) {
        elder.setUpdatedAt(LocalDateTime.now());
        elderMapper.updateById(elder);
    }

    @Override
    public List<Elder> findUnassigned() {return elderMapper.findUnassigned();}
}
