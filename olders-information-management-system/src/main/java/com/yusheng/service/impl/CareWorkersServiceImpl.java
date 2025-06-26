package com.yusheng.service.impl;

import com.yusheng.mapper.CareWorkersMapper;
import com.yusheng.pojo.CareWorker;
import com.yusheng.service.CareWorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CareWorkersServiceImpl implements CareWorkersService {

    @Autowired
    private CareWorkersMapper careWorkersMapper;

//    查询全部护工数据
    @Override
    public List<CareWorker> findAll() {
        return careWorkersMapper.findAll();
    }

    //    删除护工
    @Override
    public void deleteById(Integer id) {
        careWorkersMapper.deleteById(id);
    }

//    新增护工
    @Override
    public void save(CareWorker careWorker) {
        careWorker.setCreatedAt(LocalDateTime.now());
        careWorker.setUpdatedAt(LocalDateTime.now());
        careWorkersMapper.save(careWorker);
    }

//    根据ID查询护工信息
    @Override
    public CareWorker getInfo(Integer id) {
        return careWorkersMapper.getById(id);
    }

//    修改护工
    @Override
    public void update(CareWorker careWorker) {
        careWorker.setUpdatedAt(LocalDateTime.now());
        careWorkersMapper.updateById(careWorker);
    }

}
