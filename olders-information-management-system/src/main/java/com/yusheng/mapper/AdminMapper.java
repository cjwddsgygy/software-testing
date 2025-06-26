package com.yusheng.mapper;

import com.yusheng.pojo.Admin;
import com.yusheng.pojo.AdminInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

//    管理员登录
    @Select("select id, name, account from admins where account = #{account}" +
            " and password = #{password}")
    Admin selectByUsernameAndPassword(Admin admin);
}
