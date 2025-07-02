package com.yusheng.controller;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.PageResult;
import com.yusheng.pojo.Result;
import com.yusheng.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequestMapping("/api/elders")
@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    //    动态查询老人数据
    @GetMapping
    public Result<PageResult<Elder>> list(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        // 1. 创建 Pageable 对象，并设置默认排序
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        // 2. 调用 Service 获取 Page<Elder> 对象
        Page<Elder> elderPage = elderService.findPage(searchTerm, pageable);

        // 3. 将 Page<Elder> 转换为前端需要的 PageResult DTO
        PageResult<Elder> result = new PageResult<>(
                elderPage.getContent(),
                elderPage.getTotalElements()
        );

        return Result.success(result);
    }

    //    删除老人
    @DeleteMapping("/{id}") // <<--- 新的、更推荐的写法

    public Result delete(@PathVariable Integer id) { // <<--- 使用 @PathVariable
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
        Elder elder = elderService.getById(id);
        return Result.success(elder);
    }

    //    修改老人
    @PutMapping("/{id}")

    public Result update(@RequestBody Elder elder) {
        elderService.update(elder);
        return Result.success();
    }
}
