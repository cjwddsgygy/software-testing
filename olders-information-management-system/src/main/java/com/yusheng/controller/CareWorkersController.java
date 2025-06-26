package com.yusheng.controller;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Result;
import com.yusheng.service.CareWorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/careWorkers")
@RestController
public class CareWorkersController {

    @Autowired
    private CareWorkersService careWorkersService;

//    查询全部护工数据
    @GetMapping
    public Result list() {
        List<CareWorker> careWorkerList = careWorkersService.findAll();
        return Result.success(careWorkerList);
    }

//    删除护工
    @DeleteMapping
    public Result delete(Integer id) {
        careWorkersService.deleteById(id);
        return Result.success();
    }

//    新增护工
    @PostMapping
    public Result save(@RequestBody CareWorker careWorker) {
        careWorkersService.save(careWorker);
        return Result.success();
    }

//    根据ID查询护工信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        CareWorker careWorker = careWorkersService.getInfo(id);
        return Result.success(careWorker);
    }

//    修改护工
    @PutMapping
    public Result update(@RequestBody CareWorker careWorker) {
        careWorkersService.update(careWorker);
        return Result.success();
    }
}
