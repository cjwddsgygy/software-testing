package com.yusheng.mapper;

import com.yusheng.pojo.CareWorker;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CareWorkersMapper {

//    查询全部护工数据
    List<CareWorker> findAll();

//    删除护工
    void deleteById(Integer id);

//     新增护工
    void save(CareWorker careWorker);

//    根据ID查询护工信息
    CareWorker getById(Integer id);

//    修改护工
    void updateById(CareWorker careWorker);
}
