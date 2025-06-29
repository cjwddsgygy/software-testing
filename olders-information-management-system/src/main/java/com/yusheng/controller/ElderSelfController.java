package com.yusheng.controller;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.Result;
import com.yusheng.service.ElderSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/elderSelf/{id}")
@RestController
public class ElderSelfController {

    @Autowired
    public ElderSelfService elderSelfService;

    //    根据ID查询老人信息
    @GetMapping
    public Result getInfo(@PathVariable Integer id) {
        Elder elder = elderSelfService.getInfo(id);
        return Result.success(elder);
    }

    //    修改老人
    @PutMapping
    public Result update(@RequestBody Elder elder) {
        elderSelfService.update(elder);
        return Result.success();
    }

}
