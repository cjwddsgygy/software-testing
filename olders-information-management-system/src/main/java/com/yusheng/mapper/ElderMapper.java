package com.yusheng.mapper;

import com.yusheng.pojo.Elder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 导入 @Param 注解

import java.util.List;

@Mapper
public interface ElderMapper {

    /**
     * 根据条件动态查询老人。
     * 使用 @Param 注解是为了在 XML 中可以方便地通过名字 (id, name) 引用参数。
     * @param id 老人ID (可选)
     * @param name 老人姓名 (可选)
     * @return 老人列表
     */
    List<Elder> findElders(@Param("id") Integer id, @Param("name") String name);

    // --- 以下是你原有的方法，我们确保它们定义清晰 ---

    // 查询无床位老人数据
    List<Elder> findUnassigned();

    // 删除老人
    void deleteById(Integer id);

    // 新增老人
    void save(Elder elder);

    // 根据ID查询老人信息
    Elder getById(Integer id);

    // 修改老人
    void updateById(Elder elder);

}