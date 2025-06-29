package com.yusheng.mapper;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.HealthRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HealthRecordsMapper {

    //    查询全部健康档案
    List<HealthRecord> findAll();

    //    删除健康档案
    void deleteById(Integer id);

    //     新增健康档案
    void save(HealthRecord healthRecord);

    //    根据ID查询健康档案
    HealthRecord getById(Integer id);

    //    修改健康档案
    void updateById(HealthRecord healthRecord);
}
