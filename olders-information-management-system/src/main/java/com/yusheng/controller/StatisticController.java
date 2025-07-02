package com.yusheng.controller;

import com.yusheng.pojo.Result; // 导入您项目中的 Result 类
import com.yusheng.dto.HomeViewStatsDto;
import com.yusheng.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // 与其他控制器保持一致的基础路径
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    /**
     * 获取 HomeView 的概览统计数据。
     * 对应前端请求：GET /stats/overview
     * @return 统一响应格式的统计数据
     */
    @GetMapping("/stats/overview")
    public Result<HomeViewStatsDto> getHomeViewStatistics() {
        HomeViewStatsDto stats = statisticService.getHomeViewStatistics();
        return Result.success(stats); // 使用您项目中的 Result.success() 方法
    }

}