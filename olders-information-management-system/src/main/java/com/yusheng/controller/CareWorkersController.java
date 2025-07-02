// backend/src/main/java/com/yusheng/controller/CareWorkersController.java
package com.yusheng.controller;

import com.yusheng.pojo.PageResult;
import com.yusheng.pojo.Result; // 假设这是您的通用 Result 类
import com.yusheng.pojo.CareWorker; // 用于新增/修改操作的请求体
import com.yusheng.service.CareWorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;     // ✅ 1. 导入 Page
import org.springframework.data.domain.Pageable; // ✅ 2. 导入 Pageable

import java.util.List;

@RequestMapping("/api/careWorkers")
@RestController
public class CareWorkersController {

    @Autowired
    private CareWorkersService careWorkersService;

    // 动态查询护工数据 (支持按ID或姓名搜索，或查询全部)
    // ✅ 核心修改：合并了之前的两个 @GetMapping 方法
    @GetMapping
    public Result<PageResult<CareWorker>> findPage(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        // 创建 Pageable 对象，并设置默认排序
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        // 调用 Service 获取 Page<CareWorker> 对象
        Page<CareWorker> workerPage = careWorkersService.findPage(searchTerm, pageable);

        // 将 Page<CareWorker> 转换为前端需要的 PageResult DTO
        PageResult<CareWorker> result = new PageResult<>(
                workerPage.getContent(),
                workerPage.getTotalElements()
        );

        return Result.success(result);
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
        CareWorker careWorker = careWorkersService.getById(id);
        return Result.success(careWorker);
    }

    // 修改护工
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody CareWorker careWorker) {
        // 这是一个好习惯，确保URL中的ID和请求体中的ID一致，或者以URL中的ID为准
        careWorker.setId(id);

        // 调用 Service 层去执行更新操作
        careWorkersService.update(careWorker);

        return Result.success();
    }
}
