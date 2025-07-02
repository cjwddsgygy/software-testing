// 文件路径: src/main/java/com/yusheng/service/BedService.java
package com.yusheng.service;

import com.yusheng.pojo.Bed;
import java.util.List;

public interface BedService {

    /**
     * 根据搜索词查询床位列表
     */
    List<Bed> findBeds(String searchTerm);

    /**
     * 新增一个床位
     */
    Bed save(Bed bed);

    /**
     * 更新一个床位
     */
    Bed update(Bed bed);

    /**
     * 根据 ID 删除一个床位
     */
    void deleteById(Integer id);

    /**
     * 根据 ID 查找一个床位
     */
    Bed findById(Integer id);

    /**
     * 处理退住业务
     * @param bedId 要办理退住的床位ID
     * @return 更新后的床位对象
     */
    Bed checkout(Integer bedId);
}