package com.yusheng.service;

import com.yusheng.pojo.Admin;
import com.yusheng.pojo.AdminInfo;

public interface AdminService {

//    管理员登录
    AdminInfo login(Admin admin);
    Admin getById(Integer id); // <-- 新增这个方法
}
