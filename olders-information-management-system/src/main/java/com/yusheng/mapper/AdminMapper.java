// backend/src/main/java/com/yusheng/mapper/AdminMapper.java
package com.yusheng.mapper;

import com.yusheng.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select * from admins where account = #{account} and password = #{password}")
    Admin selectByUsernameAndPassword(Admin admin);

    // --- 新增这个方法 ---
    @Select("select * from admins where id = #{id}")
    Admin selectById(Integer id);
}
