// 文件路径: src/main/java/com/yusheng/mapper/ElderSelfMapper.java
package com.yusheng.mapper;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.ElderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElderSelfMapper {

    // --- 你已有的方法 ---
    Elder getById(Integer id);
    void updateById(Elder elder);
    Elder selectByUsernameAndPassword(Elder elder);
    ElderInfo selectById(Integer id);

    // ✅✅✅ 新增这个方法：通过账号密码查询完整的 Elder 对象 ✅✅✅
    Elder findFullByCredentials(Elder elder);
}