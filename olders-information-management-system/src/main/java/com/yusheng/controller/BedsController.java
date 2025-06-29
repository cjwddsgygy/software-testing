package com.yusheng.controller;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Result; // 确保你有这个通用的Result类
import com.yusheng.repository.BedRepository; // 注入JPA的Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/beds")
@RestController
public class BedsController {

    @Autowired
    private BedRepository bedRepository; // 直接注入Repository

    /**
     * 查询床位列表，支持按关键字搜索
     */
    @GetMapping
    public Result list(@RequestParam(required = false) String searchTerm) {
        List<Bed> bedList;
        if (StringUtils.hasText(searchTerm)) {
            // 如果有搜索词，调用自定义的查询方法
            bedList = bedRepository.findBySearchTerm(searchTerm);
        } else {
            // 如果没有搜索词，查询全部并排序
            bedList = bedRepository.findAll(Sort.by("roomNumber", "bedNumber"));
        }
        return Result.success(bedList);
    }

    /**
     * 新增床位
     */
    @PostMapping
    public Result save(@RequestBody Bed bed) {
        bed.setCreatedAt(LocalDateTime.now());
        bed.setUpdatedAt(LocalDateTime.now());
        Bed savedBed = bedRepository.save(bed);
        return Result.success(savedBed);
    }

    /**
     * 修改床位
     */
    @PutMapping
    public Result update(@RequestBody Bed bed) {
        // 检查床位是否存在
        if (bed.getId() == null || !bedRepository.existsById(bed.getId())) {
            // ✅ 修改点
            return Result.error(404, "找不到要更新的床位");
        }
        bed.setUpdatedAt(LocalDateTime.now());
        Bed updatedBed = bedRepository.save(bed); // save方法同时用于新增和更新
        return Result.success(updatedBed);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        if (!bedRepository.existsById(id)) {
            // ✅ 修改点
            return Result.error(404, "找不到要删除的床位");
        }
        bedRepository.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/checkout")
    public Result checkout(@PathVariable Integer id) {
        Optional<Bed> optionalBed = bedRepository.findById(id);
        if (optionalBed.isEmpty()) {
            // ✅ 修改点
            return Result.error(404, "找不到要办理退住的床位");
        }
        // ...
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        return bedRepository.findById(id)
                .map(Result::success)
                // ✅ 修改点
                .orElse(Result.error(404, "未找到ID为 " + id + " 的床位"));

    }
}