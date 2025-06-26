package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.HealthRecord;

import java.util.List;

public interface HealthRecordsService {

    //    查询全部健康档案
    List<HealthRecord> findAll();

    //    删除健康档案
    void deleteById(Integer id);

    //    新增健康档案
    void save(HealthRecord healthRecord);

    //    根据ID查询健康档案
    HealthRecord getInfo(Integer id);

    //    修改健康档案
    void update(HealthRecord healthRecord);
}
