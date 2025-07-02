// 文件路径: src/main/java/com/yusheng/service/impl/CareWorkerServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.CareWorker;
import com.yusheng.repository.CareWorkersRepository;
import com.yusheng.service.CareWorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;     // ✅ 1. 导入 Page
import org.springframework.data.domain.Pageable; // ✅ 2. 导入 Pageable

import java.util.Collections;
import java.util.List;

@Service
public class CareWorkersServiceImpl implements CareWorkersService {

    @Autowired
    private CareWorkersRepository careWorkersRepository;

    @Override
    @Transactional(readOnly = true)
    public CareWorker login(String account, String password) {
        return careWorkersRepository.findByAccount(account)
                .filter(worker -> worker.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public CareWorker getById(Integer id) {
        return careWorkersRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(CareWorker careWorker) {
        if (careWorker.getId() == null || !careWorkersRepository.existsById(careWorker.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + careWorker.getId() + " 的护工。");
        }
        careWorkersRepository.save(careWorker);
    }

    @Override
    @Transactional
    public CareWorker save(CareWorker careWorker) {
        // 在这里可以添加业务校验，比如账号是否已存在
        if (careWorkersRepository.findByAccount(careWorker.getAccount()).isPresent()) {
            throw new RuntimeException("新增失败：账号 " + careWorker.getAccount() + " 已存在。");
        }
        return careWorkersRepository.save(careWorker);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        careWorkersRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CareWorker> findPage(String searchTerm, Pageable pageable) {
        // 将所有查询逻辑委托给 Repository 的 findBySearchTerm 方法
        return careWorkersRepository.findBySearchTerm(searchTerm, pageable);
    }
}