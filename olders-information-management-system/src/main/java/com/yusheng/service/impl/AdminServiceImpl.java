package com.yusheng.service.impl;

import com.yusheng.mapper.AdminMapper;
import com.yusheng.pojo.Admin;
// import com.yusheng.pojo.AdminInfo; // ✅ 不再需要 AdminInfo
// import com.yusheng.utils.JwtUtils; // ✅ 不再需要 JwtUtils
import com.yusheng.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import java.util.HashMap; // ✅ 不再需要 HashMap
// import java.util.Map; // ✅ 不再需要 Map

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    // 登录方法现在只负责验证账号密码，并返回 Admin 对象
    @Override
    public Admin login(String account, String password) { // ✅ 参数修改为 String account, String password
        // 创建一个 Admin 对象用于查询
        Admin queryAdmin = new Admin();
        queryAdmin.setAccount(account);
        queryAdmin.setPassword(password);

        // 调用 Mapper 查询
        Admin authenticatedAdmin = adminMapper.selectByUsernameAndPassword(queryAdmin);

        // 如果认证成功，返回 Admin 对象；否则返回 null
        return authenticatedAdmin;
    }

    // 根据ID获取Admin信息的方法，用于JWT过滤器，保持不变
    @Override
    public Admin getById(Integer id) {
        return adminMapper.selectById(id);
    }
}