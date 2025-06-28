package com.yusheng.service.impl;

import com.yusheng.mapper.CareWorkersSelfMapper;
import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.CareWorkerInfo;
import com.yusheng.service.CareWorkersSelfService;
import com.yusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CareWorkersSelfServiceImpl implements CareWorkersSelfService {

    @Autowired
    public CareWorkersSelfMapper careWorkersSelfMapper;

//    根据ID查询护工信息
    @Override
    public CareWorker getInfo(Integer id) {
        return careWorkersSelfMapper.getById(id);
    }

//    修改护工
    @Override
    public void update(CareWorker careWorker) {
        careWorker.setUpdatedAt(LocalDateTime.now());
        careWorkersSelfMapper.updateById(careWorker);
    }

//    护工登录
    @Override
    public CareWorkerInfo login(CareWorker careWorker) {
        CareWorker careWorkerInfo = careWorkersSelfMapper.selectByUsernameAndPassword(careWorker);
        if (careWorkerInfo != null) {
//            生成JWT令牌
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", careWorkerInfo.getId());
            payload.put("account", careWorkerInfo.getAccount());
            String jwt = JwtUtils.generateToken(payload);

            return new CareWorkerInfo(careWorkerInfo.getId(), careWorkerInfo.getName(),
                    careWorkerInfo.getAccount(), jwt);
        }
        return null;
    }

    // **新增这个方法的实现**
    @Override
    public CareWorkerInfo getById(Integer id) {
        // 调用 Mapper 从数据库根据 ID 查询护工信息
        return careWorkersSelfMapper.selectById(id); // 假设您的 Mapper 有 selectById 方法
    }

}
