// 文件路径: src/main/java/com/yusheng/repository/AdminRepository.java (新建)
package com.yusheng.repository;

import com.yusheng.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // Spring Data JPA 会根据方法名自动生成查询
    // 等效于: select * from admins where account = ?
    Optional<Admin> findByAccount(String account);
}
