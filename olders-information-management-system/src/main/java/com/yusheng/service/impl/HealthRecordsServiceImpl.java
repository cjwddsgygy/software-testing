package com.yusheng.service.impl;

import com.yusheng.mapper.HealthRecordsMapper;
import com.yusheng.pojo.HealthRecord;
import com.yusheng.service.HealthRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthRecordsServiceImpl implements HealthRecordsService {

    @Autowired
    private HealthRecordsMapper healthRecordsMapper;

    //    查询全部健康档案
    @Override
    public List<HealthRecord> findAll() {
        return healthRecordsMapper.findAll();
    }

    //    删除健康档案
    @Override
    public void deleteById(Integer id) {
        healthRecordsMapper.deleteById(id);
    }

    //    新增健康档案
    @Override
    public void save(HealthRecord healthRecord) {
        healthRecord.setCreatedAt(LocalDateTime.now());
        healthRecord.setUpdatedAt(LocalDateTime.now());
        healthRecordsMapper.save(healthRecord);
    }

    //    根据ID查询健康档案
    @Override
    public HealthRecord getInfo(Integer id) {
        return healthRecordsMapper.getById(id);
    }

    //    修改健康档案
    @Override
    public void update(HealthRecord healthRecord) {
        healthRecord.setUpdatedAt(LocalDateTime.now());
        healthRecordsMapper.updateById(healthRecord);
    }

}
