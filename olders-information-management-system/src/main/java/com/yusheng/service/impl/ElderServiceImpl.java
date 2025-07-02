// 文件路径: src/main/java/com/yusheng/service/impl/ElderServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.Elder;
import com.yusheng.repository.ElderRepository; // ✅ 导入 JPA Repository
import com.yusheng.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
public class ElderServiceImpl implements ElderService {

    @Autowired
    private ElderRepository elderRepository; // ✅ 只注入 Repository

    @Override
    @Transactional(readOnly = true)
    public Elder login(String account, String password) {
        return elderRepository.findByAccount(account)
                .filter(elder -> elder.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Elder getById(Integer id) {
        // getInfo 和 getById 功能重复，我们统一用 getById
        return elderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(Elder elder) {
        if (elder.getId() == null || !elderRepository.existsById(elder.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + elder.getId() + " 的老人。");
        }
        elderRepository.save(elder);
    }

    @Override
    @Transactional
    public Elder save(Elder elder) {
        if (elderRepository.findByAccount(elder.getAccount()).isPresent()) {
            throw new RuntimeException("新增失败：账号 " + elder.getAccount() + " 已存在。");
        }
        // Pojo 里的 @CreationTimestamp 和 @UpdateTimestamp 会自动处理时间
        return elderRepository.save(elder);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        elderRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Elder> findPage(String searchTerm, Pageable pageable) {
        if (StringUtils.hasText(searchTerm)) {
            // 尝试将搜索词解析为ID
            try {
                Integer id = Integer.parseInt(searchTerm);
                // 如果是数字，我们既可以按ID搜，也可以按姓名搜
                return elderRepository.findByIdOrNameContaining(id, searchTerm, pageable); // ⚠️ 需要在 Repository 添加新方法
            } catch (NumberFormatException e) {
                // 如果不是数字，只按姓名搜
                return elderRepository.findByNameContaining(searchTerm, pageable);
            }
        }
        // 如果没有搜索词，查询所有并分页
        return elderRepository.findAll(pageable);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<Elder> findUnassigned(Pageable pageable) {
        return elderRepository.findUnassigned(pageable);
    }
}
