package com.yusheng.controller;

import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Result;
import com.yusheng.service.CareWorkersSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/careWorkersSelf/{id}")
@RestController
public class CareWorkersSelfController {

    @Autowired
    public CareWorkersSelfService careWorkersSelfService;

    //    根据ID查询护工信息
    @GetMapping
    public Result getInfo(@PathVariable Integer id) {
        CareWorker careWorker = careWorkersSelfService.getInfo(id);
        return Result.success(careWorker);
    }

    //    修改护工
    @PutMapping
    public Result update(@RequestBody CareWorker careWorker) {
        careWorkersSelfService.update(careWorker);
        return Result.success();
    }

}