// src/main/java/com/yusheng/controller/BedsController.java
package com.yusheng.controller;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Elder; // *** 导入 Elder POJO ***
import com.yusheng.pojo.Result;
import com.yusheng.service.BedsService;
import com.yusheng.service.ElderService; // *** 注入 EldersService ***
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/beds")
@RestController
public class BedsController {

    @Autowired
    private BedsService bedsService;

    @Autowired
    private ElderService elderService; // *** 注入EldersService，用于查询老人信息 ***

    /**
     * 查询床位列表 (已升级为支持分页和条件查询)
     * @param status 状态 (空闲, 已入住, 维修中)
     * @param search 床位号关键字
     * @return 床位列表
     */
    @GetMapping
    public Result list(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String search) {
        List<Bed> bedList = bedsService.findPage(status, search);
        return Result.success(bedList);
    }

    //    删除床位 (参数类型改为 Integer)
    @DeleteMapping("/{id}") // *** 改为路径变量，更符合RESTful风格 ***
    public Result delete(@PathVariable Integer id) {
        bedsService.deleteById(id);
        return Result.success();
    }

    //    新增床位
    @PostMapping
    public Result save(@RequestBody Bed bed) {
        // 新增时设置创建和更新时间
        bed.setCreatedAt(LocalDateTime.now());
        bed.setUpdatedAt(LocalDateTime.now());
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
        // 修改时只更新 `updatedAt`
        bed.setUpdatedAt(LocalDateTime.now());
        bedsService.update(bed);
        return Result.success();
    }

    /**
     * *** 新增接口：获取所有未入住的老人列表 ***
     * (用于前端“办理入住”的下拉框)
     * @return 未分配床位的老人列表
     */
    @GetMapping("/unassigned-elders")
    public Result getUnassignedElders() {
        List<Elder> elders = elderService.findUnassigned();
        return Result.success(elders);
    }
}
