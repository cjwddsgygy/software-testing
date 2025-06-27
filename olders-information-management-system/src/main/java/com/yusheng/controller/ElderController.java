package com.yusheng.controller;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.Result;
import com.yusheng.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/elders")
@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    //    查询全部老人数据
    @GetMapping
    public Result list(String name) { // ✅ 增加 String name 参数
        List<Elder> elderList = elderService.findAll(name); // ✅ 调用 service 的新方法
        return Result.success(elderList);
    }

    //    删除老人
    @DeleteMapping
    public Result delete(Integer id) {
        elderService.deleteById(id);
        return Result.success();
    }

    //    新增老人
    @PostMapping
    public Result save(@RequestBody Elder elder) {
        elderService.save(elder);
        return Result.success();
    }

    //    根据ID查询老人信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Elder elder = elderService.getInfo(id);
        return Result.success(elder);
    }

    //    修改老人
    @PutMapping
    public Result update(@RequestBody Elder elder) {
        elderService.update(elder);
        return Result.success();
    }
}
