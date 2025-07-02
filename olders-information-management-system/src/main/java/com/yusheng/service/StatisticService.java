// 文件路径: src/main/java/com/yusheng/service/StatisticService.java
package com.yusheng.service;

import com.yusheng.dto.HomeViewStatsDto;
import com.yusheng.pojo.Bed; // ✅ 1. 导入 Bed 实体
import com.yusheng.repository.BedRepository;
import com.yusheng.repository.CareWorkersRepository; // ✅ 确保命名统一
import com.yusheng.repository.ElderRepository;
import com.yusheng.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CareWorkersRepository careWorkersRepository; // ✅ 确保命名统一

    /**
     * 获取 HomeView 所需的概览统计数据。
     */
    public HomeViewStatsDto getHomeViewStatistics() {
        long totalOlders = elderRepository.count();
        long totalBeds = bedRepository.count();

        // ✅✅✅ 核心修复：使用枚举实例 Bed.BedStatus.空闲 进行调用 ✅✅✅
        long availableBeds = bedRepository.countByStatus("空闲");

        long totalCareWorkers = careWorkersRepository.count();
        long totalExpenses = expenseRepository.countExpensesThisMonth();

        return new HomeViewStatsDto(
                totalOlders,
                totalBeds,
                availableBeds,
                totalExpenses,
                totalCareWorkers
        );
    }
}