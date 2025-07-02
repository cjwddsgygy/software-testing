// backend/src/main/java/com/yusheng/dto/CareWorkerDto.java
package com.yusheng.dto;

import com.fasterxml.jackson.annotation.JsonFormat; // 导入 JsonFormat
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareWorkerDto {
    private Integer id;
    private String name;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private String birthDate;
    private String ethnicity;
    private String account;
    private String education;
    private String experience;
    private String specialties;
    // 不直接暴露 assignedElders 原始字符串，而是通过 elderCount
    // private String assignedElders; // 如果需要前端显示原始列表，可以取消注释

    private Integer elderCount; // 护工负责老人数量

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;

    // TODO: 如果需要，可以添加一个字段用于前端展示“联系方式”或“性别”，但这需要后端 CareWorker POJO/DB 表中实际有这些字段
    // 目前根据 care_workers.txt，没有 gender 或 contact 字段。
    // 为了与前端 CareWorkerView.vue 的初始表格保持兼容，这里添加模拟字段。
    // 实际项目中，如果数据库没有，应删除前端表格列或在后端补充字段。
    private String gender; // 暂时添加，用于前端表格展示，但数据库无对应字段
    private String contact; // 暂时添加，用于前端表格展示，但数据库无对应字段
    private String hireDate; // 暂时添加，用于前端表格展示，但数据库无对应字段
}
