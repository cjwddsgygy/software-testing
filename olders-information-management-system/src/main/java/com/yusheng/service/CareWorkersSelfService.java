package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.CareWorkerInfo;

public interface CareWorkersSelfService {

//    根据ID查询护工信息
    CareWorker getInfo(Integer id);

//    修改护工
    void update(CareWorker careWorker);

//    护工登录
    CareWorkerInfo login(CareWorker careWorker);
}
