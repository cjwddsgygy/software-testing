package com.yusheng.service;

import com.yusheng.pojo.Admin;
import org.apache.ibatis.annotations.Update;

import java.util.List;
// import com.yusheng.pojo.AdminInfo; // ✅ 不再需要 AdminInfo

public interface AdminService {
    // 登录方法现在直接返回 Admin 对象
    Admin login(String account, String password); // 或者 Admin login(Admin admin);

    // 根据ID获取Admin信息的方法，用于JWT过滤器
    Admin getById(Integer id);

    /**
     * ✅✅✅ 新增这个 update 方法的声明 ✅✅✅
     */
    void update(Admin admin);
    // ✅ 新增这个方法
    List<Admin> findAll();

    // ✅ 新增这个方法
    void deleteById(Integer id);

    // ✅ 修改 save 方法，使其返回保存后的对象
    Admin save(Admin admin);


}