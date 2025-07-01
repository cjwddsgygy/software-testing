package com.yusheng.service;

import com.yusheng.pojo.Admin;
// import com.yusheng.pojo.AdminInfo; // ✅ 不再需要 AdminInfo

public interface AdminService {
    // 登录方法现在直接返回 Admin 对象
    Admin login(String account, String password); // 或者 Admin login(Admin admin);

    // 根据ID获取Admin信息的方法，用于JWT过滤器
    Admin getById(Integer id);
}