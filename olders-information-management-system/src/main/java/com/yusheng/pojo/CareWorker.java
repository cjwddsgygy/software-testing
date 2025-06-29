// backend/src/main/java/com/yusheng/pojo/CareWorker.java
package com.yusheng.pojo;

import com.fasterxml.jackson.annotation.JsonFormat; // 导入 JsonFormat
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity; // 新增导入
import jakarta.persistence.Id; // 新增导入
import jakarta.persistence.GeneratedValue; // 新增导入
import jakarta.persistence.GenerationType; // 新增导入
import jakarta.persistence.Table; // 新增导入

import java.time.LocalDateTime; // 使用 LocalDateTime 对应数据库 datetime 类型

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // <<--- 必须添加这个注解
@Table(name = "care_workers") // <<--- 推荐添加，确保映射到正确的表名，通常是小写单数
public class CareWorker {
    @Id // <<--- 必须标注主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <<--- 标注主键生成策略，IDENTITY 表示数据库自增
    private Integer id;
    private String name;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai") // ✅ 格式化日期，只显示年月日
    private String birthDate; // 对应数据库的 birth_date 字段
    private String ethnicity;
    private String account;
    private String status;
    private String password;
    private String education;
    private String experience;
    private String specialties;
    private String assignedElders; // 负责老人列表（可存ID数组或JSON），前端用于计算数量

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai") // ✅ 格式化时间
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai") // ✅ 格式化时间
    private LocalDateTime updatedAt;
}