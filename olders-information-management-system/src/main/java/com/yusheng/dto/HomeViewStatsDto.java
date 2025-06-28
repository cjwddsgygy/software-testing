package com.yusheng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeViewStatsDto {
    private long totalOlders;
    private long totalBeds;
    private long availableBeds;
    private long totalExpenses; // 收费项目种类数
    private long totalCareworkers;//护工总数
}