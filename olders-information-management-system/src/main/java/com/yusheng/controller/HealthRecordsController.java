// 文件路径: backend/src/main/java/com/yusheng/controller/HealthRecordsController.java
package com.yusheng.controller;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.pojo.PageResult;
import com.yusheng.pojo.Result;
import com.yusheng.service.HealthRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/healthRecords")
public class HealthRecordsController {

    @Autowired
    private HealthRecordsService healthRecordsService;

    // 分页和搜索查询
    @GetMapping
    public Result<PageResult<HealthRecord>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("recordDate").descending());
        Page<HealthRecord> pageResult = healthRecordsService.findPage(search, pageable);

        // ✅✅✅ 核心修复：颠倒构造函数的参数顺序 ✅✅✅
        // 将 (总数, 列表) 改为 (列表, 总数)
        PageResult<HealthRecord> result = new PageResult<>(
                pageResult.getContent(),      // 第一个参数：数据列表
                pageResult.getTotalElements() // 第二个参数：总记录数
        );

        return Result.success(result);
    }

    // 新增健康档案
    @PostMapping
    public Result<HealthRecord> save(@RequestBody HealthRecord healthRecord) {
        HealthRecord savedRecord = healthRecordsService.save(healthRecord);
        return Result.success(savedRecord);
    }

    // 根据ID查询健康档案
    @GetMapping("/{id}")
    public Result<HealthRecord> getById(@PathVariable Integer id) {
        HealthRecord healthRecord = healthRecordsService.findById(id);
        if (healthRecord == null) {
            return Result.error("记录不存在");
        }
        return Result.success(healthRecord);
    }

    // 修改健康档案
    @PutMapping("/{id}")
    public Result<HealthRecord> update(@PathVariable Integer id, @RequestBody HealthRecord healthRecord) {
        healthRecord.setId(id);
        HealthRecord updatedRecord = healthRecordsService.update(healthRecord);
        return Result.success(updatedRecord);
    }

    // 删除健康档案
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        healthRecordsService.deleteById(id);
        return Result.success();
    }
}