package com.yusheng.service;

import com.yusheng.repository.BedRepository;       // ✅ 步骤1: 导入 BedRepository
import com.yusheng.repository.CareWorkersRepository;
import com.yusheng.repository.ElderRepository;
import com.yusheng.dto.HomeViewStatsDto;             // 确保你的 DTO 类路径正确
import com.yusheng.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.SQLException;

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
    private ExpenseRepository expenseRepository;

    @Autowired
    private CareWorkersRepository careworkersRepository;

    /**
     * 获取 HomeView 所需的概览统计数据。
     * @return 包含各项统计数据的 DTO
     */



    @Autowired
    private DataSource dataSource; // ✅ 注入主数据源

    public HomeViewStatsDto getHomeViewStatistics() {

        // ✅✅✅ 添加这段临时的调试代码 ✅✅✅
        try {
            System.out.println("==========================================================");
            System.out.println("JPA is using a database connection with URL:");
            System.out.println(dataSource.getConnection().getMetaData().getURL());
            System.out.println("==========================================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // ✅✅✅ 调试代码结束 ✅✅✅

        long totalOlders = elderRepository.count();
        long totalBeds = bedRepository.count();
        long availableBeds = bedRepository.countByStatus("空闲");
        long totalCareworkers = careworkersRepository.count();
        long totalExpenses = expenseRepository.countExpensesThisMonth();

        return new HomeViewStatsDto(totalOlders, totalBeds, availableBeds, totalExpenses, totalCareworkers);
    }
}
