// 文件路径: src/main/java/com/yusheng/service/impl/AdminServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.Admin;
import com.yusheng.repository.AdminRepository;
import com.yusheng.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    // 假设您不需要密码加密器

    @Override
    @Transactional(readOnly = true)
    public Admin login(String account, String password) {
        return adminRepository.findByAccount(account)
                .filter(admin -> admin.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(Admin admin) {
        if (admin.getId() == null || !adminRepository.existsById(admin.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + admin.getId() + " 的管理员。");
        }
        adminRepository.save(admin);
    }

    /**
     * ✅✅✅ 核心修复：补上这个缺失的 save 方法的实现 ✅✅✅
     */
    @Override
    @Transactional
    public Admin save(Admin admin) {
        // 在新增前，可以检查账号是否已存在，防止重复
        if (adminRepository.findByAccount(admin.getAccount()).isPresent()) {
            throw new RuntimeException("新增失败：账号 " + admin.getAccount() + " 已存在。");
        }
        // 调用JPA的save方法，它会返回保存后带有ID的实体对象
        return adminRepository.save(admin);
    }

    /**
     * ✅✅✅ 同时，为了完整性，我们把 findAll 和 deleteById 也补上 ✅✅✅
     */
    @Override
    @Transactional(readOnly = true)
    public java.util.List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        adminRepository.deleteById(id);
    }
}