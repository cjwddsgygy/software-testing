// 文件路径: src/main/java/com/yusheng/service/impl/ElderSelfServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.mapper.ElderSelfMapper;
import com.yusheng.pojo.Elder;
import com.yusheng.pojo.ElderInfo;
import com.yusheng.service.ElderSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ElderSelfServiceImpl implements ElderSelfService {

    @Autowired
    private ElderSelfMapper elderSelfMapper; // ✅ 确保注入的是 private

    @Override
    public Elder getInfo(Integer id) {
        return elderSelfMapper.getById(id);
    }

    @Override
    public void update(Elder elder) {
        elder.setUpdatedAt(LocalDateTime.now());
        elderSelfMapper.updateById(elder);
    }

    @Override
    public ElderInfo login(Elder elder) {
        // 保留旧逻辑
        Elder result = elderSelfMapper.selectByUsernameAndPassword(elder);
        if (result != null) {
            return new ElderInfo(result.getId(), result.getName(), result.getAccount(), null);
        }
        return null;
    }

    /**
     * ✅✅✅ 核心修复 1: 实现接口中新增的 getById(Integer id) 方法 ✅✅✅
     */
    @Override
    public ElderInfo getById(Integer id) {
        return elderSelfMapper.selectById(id);
    }

    /**
     * ✅✅✅ 核心修复 2: 实现 findFullElderByCredentials 方法 ✅✅✅
     */
    @Override
    public Elder findFullElderByCredentials(Elder elder) {
        return elderSelfMapper.findFullByCredentials(elder);
    }
}