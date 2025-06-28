package com.yusheng.controller;

import com.yusheng.pojo.HealthRecord;
import com.yusheng.pojo.Result;
import com.yusheng.service.HealthRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/healthRecords")
@RestController
public class HealthRecordsController {

    @Autowired
    private HealthRecordsService healthRecordsService;

    //    查询全部健康档案
    @GetMapping
    public Result list() {
        List<HealthRecord> healthRecordList = healthRecordsService.findAll();
        return Result.success(healthRecordList);
    }

    //    删除健康档案
    @DeleteMapping
    public Result delete(Integer id) {
        healthRecordsService.deleteById(id);
        return Result.success();
    }

    //    新增健康档案
    @PostMapping
    public Result save(@RequestBody HealthRecord healthRecord) {
        healthRecordsService.save(healthRecord);
        return Result.success();
    }

    //    根据ID查询健康档案
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        HealthRecord healthRecord = healthRecordsService.getInfo(id);
        return Result.success(healthRecord);
    }

    //    修改健康档案
    @PutMapping
    public Result update(@RequestBody HealthRecord healthRecord) {
        healthRecordsService.update(healthRecord);
        return Result.success();
    }
}
