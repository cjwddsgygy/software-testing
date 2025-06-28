package com.yusheng.controller;

import com.yusheng.pojo.Elder;
import com.yusheng.pojo.Result;
import com.yusheng.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/elders")
@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    //    动态查询老人数据
    @GetMapping
    public Result<List<Elder>> list(@RequestParam(required = false) String searchTerm) {
        Integer id = null;
        String name = null;

        // 判断 searchTerm 是否不为空
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // 尝试将 searchTerm 转换为数字
            try {
                id = Integer.parseInt(searchTerm);
                // 如果成功转换为数字，我们就认为用户想按 ID 搜索
                // 为了避免意外地也按名字搜索，将 name 保持为 null
            } catch (NumberFormatException e) {
                // 如果转换失败（说明不是纯数字），我们就认为用户想按姓名搜索
                name = searchTerm;
            }
        }

        // 调用 Service，此时 id 和 name 最多只有一个有值
        List<Elder> elderList = elderService.findElders(id, name);
        return Result.success(elderList);
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
