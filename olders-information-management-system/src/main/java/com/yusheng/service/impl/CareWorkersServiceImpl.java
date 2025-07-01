// backend/src/main/java/com/yusheng/service/impl/CareWorkersServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.mapper.CareWorkersMapper;
import com.yusheng.pojo.CareWorker;
import com.yusheng.service.CareWorkersService;
import com.yusheng.dto.CareWorkerDto; // 导入 CareWorkerDto
import org.springframework.beans.BeanUtils; // 导入 BeanUtils 用于属性拷贝
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // 导入 StringUtils

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareWorkersServiceImpl implements CareWorkersService {

    @Autowired
    private CareWorkersMapper careWorkersMapper;


    //查询护工
    @Override
    public List<CareWorker> findCareWorkers(Integer id, String name) {
        return careWorkersMapper.findCareWorkers(id, name);
    }


    // 删除护工
    @Override
    public void deleteById(Integer id) {
        careWorkersMapper.deleteById(id);
    }

    // 新增护工
    @Override
    public void save(CareWorker careWorker) {
        // 在保存前设置创建和更新时间
        careWorker.setCreatedAt(LocalDateTime.now());
        careWorker.setUpdatedAt(LocalDateTime.now());
        // 密码通常需要加密处理，这里假设前端或更高层已经处理
        careWorkersMapper.save(careWorker);
    }

    // 根据ID查询护工信息
    @Override
    public CareWorker getInfo(Integer id) {
        return careWorkersMapper.getById(id);
    }

    // 修改护工
    @Override
    public void update(CareWorker careWorker) {
        // 在更新前设置更新时间
        careWorker.setUpdatedAt(LocalDateTime.now());
        careWorkersMapper.updateById(careWorker);
    }



}
