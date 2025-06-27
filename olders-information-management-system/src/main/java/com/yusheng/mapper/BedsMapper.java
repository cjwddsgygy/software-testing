// src/main/java/com/yusheng/mapper/BedsMapper.java
package com.yusheng.mapper;

import com.yusheng.pojo.Bed;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BedsMapper {

    /**
     * 分页和条件查询床位信息（包含老人姓名）
     *
     * @return 床位列表
     */
    List<Bed> findPage();

    //    删除床位
    void deleteById(Integer id);

    //     新增床位
    void save(Bed bed);

    //    根据ID查询床位信息
    Bed getById(Integer id);

    //    修改床位
    void updateById(Bed bed);

    List<Bed> findAll();
}