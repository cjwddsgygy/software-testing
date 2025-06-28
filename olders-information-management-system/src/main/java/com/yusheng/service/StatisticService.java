package com.yusheng.service;

import com.yusheng.repository.BedRepository;
import com.yusheng.repository.ExpensesRepository;
import com.yusheng.repository.ElderRepository;
import com.yusheng.dto.HomeViewStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    @Autowired
    private ElderRepository elderRepository;

    @Autowired
    private BedRepository bedRepository; // 现在 BedRepository 已经存在了

    @Autowired
    private ExpensesRepository expensesRepository; // 确保注入的是 ChargeRepository

    /**
     * 获取 HomeView 所需的概览统计数据。
     * @return 包含各项统计数据的 DTO
     */
    public HomeViewStatsDto getHomeViewStatistics() {
        long totalOlders = elderRepository.count(); // 获取老人总数
        long totalBeds = bedRepository.count(); // 获取床位总数
        long availableBeds = bedRepository.countByStatus("空闲"); // 获取状态为"空闲"的床位总数
        long totalCharges = expensesRepository.count(); // 获取收费项目种类总数

        return new HomeViewStatsDto(totalOlders, totalBeds, availableBeds, totalCharges);
    }
}
