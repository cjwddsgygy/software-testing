package com.yusheng.mapper;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.ElderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElderSelfMapper {

//    根据ID查询老人信息
    Elder getById(Integer Id);

//    修改老人
    void updateById(Elder elder);

//    老人登录
    Elder selectByUsernameAndPassword(Elder elder);

    ElderInfo selectById(Integer id);
}
