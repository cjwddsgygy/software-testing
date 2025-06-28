// backend/src/main/java/com/yusheng/controller/CareWorkersController.java
package com.yusheng.controller;

import com.yusheng.pojo.Result; // 假设这是您的通用 Result 类
import com.yusheng.pojo.CareWorker; // 用于新增/修改操作的请求体
import com.yusheng.service.CareWorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/careWorkers")
@RestController
public class CareWorkersController {

    @Autowired
    private CareWorkersService careWorkersService;

    // 动态查询护工数据 (支持按ID或姓名搜索，或查询全部)
    // ✅ 核心修改：合并了之前的两个 @GetMapping 方法
    @GetMapping
    public Result<List<CareWorker>> list(@RequestParam(required = false) String searchTerm) {
        Integer id = null;
        String name = null;

        // 判断 searchTerm 是否不为空
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // 尝试将 searchTerm 转换为数字
            try {
                id = Integer.parseInt(searchTerm);
                // 如果成功转换为数字，我们就认为用户想按 ID 搜索
                // 为了避免意外地也按名字搜索，将 name 保持为 null
            } catch (NumberFormatException e) {
                // 如果转换失败（说明不是纯数字），我们就认为用户想按姓名搜索
                name = searchTerm;
            }
        }

        // 调用 Service 层方法进行查询，此时 id 和 name 最多只有一个有值。
        // 你需要在 CareWorkersService 中实现一个 findCareWorkers 方法来支持按ID和姓名搜索。
        // 如果 id 和 name 都为 null，Service 方法应该返回所有护工。
        List<CareWorker> careWorkerList = careWorkersService.findCareWorkers(id, name);
        return Result.success(careWorkerList);
    }

    // 删除护工
    @DeleteMapping("/{id}") // ✅ 修改为使用 @PathVariable，更符合 RESTful 风格
    public Result delete(@PathVariable Integer id) { // ✅ 确保使用 @PathVariable 接收 ID
        careWorkersService.deleteById(id);
        return Result.success();
    }

    // 新增护工
    @PostMapping
    public Result save(@RequestBody CareWorker careWorker) {
        careWorkersService.save(careWorker);
        return Result.success();
    }

    // 根据ID查询护工信息
    @GetMapping("/{id}")
    public Result<CareWorker> getInfo(@PathVariable Integer id) {
        CareWorker careWorker = careWorkersService.getInfo(id);
        return Result.success(careWorker);
    }

    // 修改护工
    @PutMapping
    public Result update(@RequestBody CareWorker careWorker) {
        careWorkersService.update(careWorker);
        return Result.success();
    }
}
