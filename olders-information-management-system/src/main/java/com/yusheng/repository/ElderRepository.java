
package com.yusheng.repository;

import com.yusheng.pojo.Elder; // 导入 Elder 实体类
import org.springframework.data.jpa.repository.JpaRepository; // 导入 JpaRepository
import org.springframework.stereotype.Repository; // 导入 Repository 注解

@Repository // 标记为一个 Spring Bean
public interface ElderRepository extends JpaRepository<Elder, Integer> { // Elder 的主键是 Integer 类型
    // JpaRepository 提供了基本的 CRUD 操作，包括 count() 方法
}