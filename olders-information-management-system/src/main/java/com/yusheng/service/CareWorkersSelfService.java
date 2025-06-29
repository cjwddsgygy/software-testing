// 文件路径: src/main/java/com/yusheng/service/CareWorkersSelfService.java
package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.CareWorkerInfo;

public interface CareWorkersSelfService {

    CareWorker getInfo(Integer id);
    void update(CareWorker careWorker);
    CareWorkerInfo login(CareWorker careWorker);

    // ✅ 确保这个方法的返回类型是 CareWorkerInfo
    CareWorkerInfo getById(Integer id);

    // ✅ 这是我们为新认证流程添加的方法
    CareWorker findFullCareWorkerByCredentials(CareWorker careWorker);
}