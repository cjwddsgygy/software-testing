package com.yusheng.controller;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.pojo.PageResult;
import com.yusheng.pojo.Result;
import com.yusheng.service.HealthRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/healthRecords")
@RestController
public class HealthRecordsController {

    @Autowired
    private HealthRecordsService healthRecordsService;

    // 分页和搜索查询
    @GetMapping
    public Result<PageResult<HealthRecord>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        // Pageable 的页码是从0开始的，所以前端传来的页码需要减1
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("updatedAt").descending());
        PageResult<HealthRecord> PageResult = healthRecordsService.findPage(search, pageable);
        return Result.success(PageResult);
    }

    // 新增健康档案
    @PostMapping
    public Result<HealthRecord> save(@RequestBody HealthRecord healthRecord) {
        HealthRecord savedRecord = healthRecordsService.save(healthRecord);
        return Result.success(savedRecord);
    }

    // 根据ID查询健康档案 (用于详情页)
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
        // 确保正在更新的是正确的记录
        healthRecord.setId(id);
        HealthRecord updatedRecord = healthRecordsService.save(healthRecord);
        return Result.success(updatedRecord);
    }

    // 删除健康档案
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        healthRecordsService.deleteById(id);
        return Result.success();
    }
}
