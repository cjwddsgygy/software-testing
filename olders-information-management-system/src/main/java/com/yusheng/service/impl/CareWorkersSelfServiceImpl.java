// 文件路径: src/main/java/com/yusheng/service/impl/CareWorkersSelfServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.mapper.CareWorkersSelfMapper;
import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.CareWorkerInfo;
import com.yusheng.service.CareWorkersSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CareWorkersSelfServiceImpl implements CareWorkersSelfService {

    @Autowired
    private CareWorkersSelfMapper careWorkersSelfMapper; // ✅ 确保注入的是 private

    @Override
    public CareWorker getInfo(Integer id) {
        return careWorkersSelfMapper.getById(id);
    }

    @Override
    public void update(CareWorker careWorker) {
        careWorker.setUpdatedAt(LocalDateTime.now());
        careWorkersSelfMapper.updateById(careWorker);
    }

    @Override
    public CareWorkerInfo login(CareWorker careWorker) {
        // 这个方法现在只被旧逻辑使用，可以保留，但新认证流程不再直接调用它
        CareWorker result = careWorkersSelfMapper.selectByUsernameAndPassword(careWorker);
        if (result != null) {
            // 注意：这里返回的是不完整的 CareWorkerInfo
            return new CareWorkerInfo(result.getId(), result.getName(), result.getAccount(), null);
        }
        return null;
    }

    /**
     * ✅✅✅ 核心修复 1: 实现接口中新增的 getById(Integer id) 方法 ✅✅✅
     * 注意：这个方法与 getInfo 方法功能重复，但为了满足接口契约，我们必须实现它。
     * 它应该返回 CareWorkerInfo，因为这是接口中 getById(Integer id) 的返回类型。
     */
    @Override
    public CareWorkerInfo getById(Integer id) {
        return careWorkersSelfMapper.selectById(id);
    }

    /**
     * ✅✅✅ 核心修复 2: 实现 findFullCareWorkerByCredentials 方法 ✅✅✅
     * 这个方法是新认证流程的关键，它必须返回完整的 CareWorker 对象。
     */
    @Override
    public CareWorker findFullCareWorkerByCredentials(CareWorker careWorker) {
        // 调用 Mapper 中返回完整 CareWorker 对象的方法
        return careWorkersSelfMapper.findFullByCredentials(careWorker);
    }
}
