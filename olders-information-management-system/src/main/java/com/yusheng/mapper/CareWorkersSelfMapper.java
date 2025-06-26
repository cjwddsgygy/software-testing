package com.yusheng.mapper;

import com.yusheng.pojo.CareWorker;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CareWorkersSelfMapper {

//    根据ID查询护工信息
    CareWorker getById(Integer id);

//    修改护工
    void updateById(CareWorker careWorker);

//    护工登录
    CareWorker selectByUsernameAndPassword(CareWorker careWorker);
}
