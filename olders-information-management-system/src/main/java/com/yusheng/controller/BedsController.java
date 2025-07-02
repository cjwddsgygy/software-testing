// 文件路径: backend/src/main/java/com/yusheng/controller/BedsController.java (最终重构版)
package com.yusheng.controller;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Elder;
import com.yusheng.pojo.Result;
import com.yusheng.service.BedService; // ✅ 导入 BedService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/beds")
@RestController
public class BedsController {

    @Autowired
    private BedService bedService; // ✅ 注入 Service，而不是直接注入 Repository

    /**
     * 查询床位列表，支持按关键字搜索
     */
    @GetMapping
    public Result<List<Bed>> list(@RequestParam(required = false) String searchTerm) {
        List<Bed> bedList = bedService.findBeds(searchTerm);
        return Result.success(bedList);
    }

    /**
     * 新增床位
     */
    @PostMapping
    public Result<Bed> save(@RequestBody Bed bed) {
        // 业务逻辑应在 Service 层处理
        Bed savedBed = bedService.save(bed);
        return Result.success(savedBed);
    }

    /**
     * 修改床位
     */
    @PutMapping("/{id}")
    public Result<Bed> update(@PathVariable Integer id, @RequestBody Bed bed) {
        bed.setId(id); // 确保 ID 正确
        try {
            Bed updatedBed = bedService.update(bed);
            return Result.success(updatedBed);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除床位
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        try {
            bedService.deleteById(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取床位详情
     */
    @GetMapping("/{id}")
    public Result<Bed> getById(@PathVariable Integer id) {
        Bed bed = bedService.findById(id);
        return bed != null ? Result.success(bed) : Result.error("未找到ID为 " + id + " 的床位");
    }

    /**
     * ✅✅✅ 核心重构：退住操作的正确实现 ✅✅✅
     */
    @PutMapping("/{id}/checkout")
    public Result<Bed> checkout(@PathVariable Integer id) {
        try {
            // 将复杂的业务逻辑完全委托给 Service 层
            Bed checkedOutBed = bedService.checkout(id);
            return Result.success(checkedOutBed);
        } catch (RuntimeException e) {
            // 捕获 Service 层可能抛出的异常（如“床位找不到”），并返回给前端
            return Result.error(e.getMessage());
        }
    }
}