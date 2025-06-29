package com.yusheng.service;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.ElderInfo;

public interface ElderSelfService {

//    根据ID查询老人信息
    Elder getInfo(Integer id);

//    修改老人
    void update(Elder elder);

//    老人登录
    ElderInfo login(Elder elder);

    // **新增这个方法声明**
    ElderInfo getById(Integer id);

    Elder findFullElderByCredentials(Elder request);
}
