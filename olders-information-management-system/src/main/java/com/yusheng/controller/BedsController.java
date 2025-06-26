package com.yusheng.controller;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Result;
import com.yusheng.service.BedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/beds")
@RestController
public class BedsController {

    @Autowired
    private BedsService bedsService;

    //    查询全部床位情况
    @GetMapping
    public Result list() {
        List<Bed> bedList = bedsService.findAll();
        return Result.success(bedList);
    }

    //    删除床位
    @DeleteMapping
    public Result delete(Integer id) {
        bedsService.deleteById(id);
        return Result.success();
    }

    //    新增床位
    @PostMapping
    public Result save(@RequestBody Bed bed) {
        bedsService.save(bed);
        return Result.success();
    }

    //    根据ID查询床位信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Bed bed = bedsService.getInfo(id);
        return Result.success(bed);
    }

    //    修改床位
    @PutMapping
    public Result update(@RequestBody Bed bed) {
        bedsService.update(bed);
        return Result.success();
    }
}
