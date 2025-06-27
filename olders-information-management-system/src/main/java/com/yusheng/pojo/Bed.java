package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {
    private Integer id;
    private String roomNumber;
    private String bedNumber;
    private String status;
    private Integer eldersId; // *** 新增：对应数据库的 elders_id 字段 ***
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // *** 新增：用于多表查询时接收老人姓名，数据库中无此字段 ***
    private String eldersName;
}
