package com.yusheng.service;

import com.yusheng.repository.BedRepository;       // ✅ 步骤1: 导入 BedRepository
import com.yusheng.repository.CareWorkersRepository;
import com.yusheng.repository.ElderRepository;
import com.yusheng.repository.ExpensesRepository;
import com.yusheng.dto.HomeViewStatsDto;             // 确保你的 DTO 类路径正确
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    @Autowired
    private ElderRepository elderRepository;

    // =======================================================
    // ✅ 步骤2: 在这里添加对 BedRepository 的注入
    // =======================================================
    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private CareWorkersRepository careworkersRepository;

    /**
     * 获取 HomeView 所需的概览统计数据。
     * @return 包含各项统计数据的 DTO
     */
    public HomeViewStatsDto getHomeViewStatistics() {
        long totalOlders = elderRepository.count();
        // ✅ 现在这两行代码可以正常工作了
        long totalBeds = bedRepository.count();
        long availableBeds = bedRepository.countByStatus("空闲");
        long totalExpenses = expensesRepository.count();
        long totalCareWorkers = careworkersRepository.count();

        return new HomeViewStatsDto(totalOlders, totalBeds, availableBeds, totalExpenses, totalCareWorkers);
    }
}
