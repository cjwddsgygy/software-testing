// 文件路径: src/main/java/com/yusheng/mapper/CareWorkersSelfMapper.java
package com.yusheng.mapper;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.CareWorkerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CareWorkersSelfMapper {

    // --- 你已有的方法 ---
    CareWorker getById(Integer id);
    void updateById(CareWorker careWorker);
    CareWorker selectByUsernameAndPassword(CareWorker careWorker); // 这个方法可能返回不完整的对象
    CareWorkerInfo selectById(Integer id);

    // ✅✅✅ 新增这个方法：通过账号密码查询完整的 CareWorker 对象 ✅✅✅
    CareWorker findFullByCredentials(CareWorker careWorker);
}