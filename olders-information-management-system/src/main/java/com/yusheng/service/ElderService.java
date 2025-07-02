// 文件路径: src/main/java/com/yusheng/service/ElderService.java
package com.yusheng.service;

import com.yusheng.pojo.Elder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElderService {
    Elder login(String account, String password);
    Elder getById(Integer id);
    void update(Elder elder);
    Elder save(Elder elder);
    void deleteById(Integer id);
    Page<Elder> findPage(String searchTerm, Pageable pageable);
    Page<Elder> findUnassigned(Pageable pageable);
}