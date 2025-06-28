package com.yusheng.service.impl;

import com.yusheng.mapper.ElderSelfMapper;
import com.yusheng.pojo.Elder;
import com.yusheng.pojo.ElderInfo;
import com.yusheng.service.ElderSelfService;
import com.yusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElderSelfServiceImpl implements ElderSelfService {

    @Autowired
    public ElderSelfMapper elderSelfMapper;

//    根据ID查询老人信息
    @Override
    public Elder getInfo(Integer id) {
        return elderSelfMapper.getById(id);
    }

//    修改老人
    @Override
    public void update(Elder elder) {
        elder.setUpdatedAt(LocalDateTime.now());
        elderSelfMapper.updateById(elder);
    }

//    老人登录
    @Override
    public ElderInfo login(Elder elder) {
        Elder elderInfo = elderSelfMapper.selectByUsernameAndPassword(elder);
        if (elderInfo != null) {
//            生成JWT令牌
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", elderInfo.getId());
            payload.put("account", elderInfo.getAccount());
            String jwt = JwtUtils.generateToken(payload);

            return new ElderInfo(elderInfo.getId(), elderInfo.getName()
                    , elderInfo.getAccount(), jwt);
        }
        return null;
    }

    // **新增这个方法的实现**
    @Override
    public ElderInfo getById(Integer id) {
        // 调用 Mapper 从数据库根据 ID 查询老人信息
        return elderSelfMapper.selectById(id); // 假设您的 Mapper 有 selectById 方法
    }
}
