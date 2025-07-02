// 文件路径: src/main/java/com/yusheng/service/CareWorkerService.java
package com.yusheng.service;

import com.yusheng.pojo.CareWorker;
import org.springframework.data.domain.Page;     // ✅ 1. 导入 Page
import org.springframework.data.domain.Pageable; // ✅ 2. 导入 Pageable

public interface CareWorkersService {

    /**
     * 验证护工登录凭证 (无加密)
     * @return 如果成功，返回完整的 CareWorker 对象；否则返回 null
     */
    CareWorker login(String account, String password);

    /**
     * 根据 ID 获取护工的完整信息
     */
    CareWorker getById(Integer id);

    /**
     * 更新护工信息
     */
    void update(CareWorker careWorker);

    /**
     * 新增一个护工
     */
    CareWorker save(CareWorker careWorker);

    /**
     * 根据 ID 删除一个护工
     */
    void deleteById(Integer id);

    /**
     * 根据 ID 或姓名动态查询护工列表
     */
    Page<CareWorker> findPage(String searchTerm, Pageable pageable);
}