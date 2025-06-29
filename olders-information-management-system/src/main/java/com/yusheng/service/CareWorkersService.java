// backend/src/main/java/com/yusheng/service/CareWorkersService.java
package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import com.yusheng.dto.CareWorkerDto; // 导入 CareWorkerDto

import java.util.List;

public interface CareWorkersService {

    // 查询全部护工数据
    List<CareWorker> findCareWorkers(Integer id, String name);

    // 删除护工
    void deleteById(Integer id);

    // 新增护工
    void save(CareWorker careWorker);

    // 根据ID查询护工信息
    CareWorker getInfo(Integer id);

    // 修改护工
    void update(CareWorker careWorker);

}