package com.yusheng.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.persistence.Entity; // <-- 新增导入
import jakarta.persistence.Id; // <-- 新增导入
import jakarta.persistence.GeneratedValue; // <-- 新增导入
import jakarta.persistence.GenerationType; // <-- 新增导入
import jakarta.persistence.Table; // <-- 新增导入 (可选，但推荐)

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity // <<--- 必须添加这个注解
@Table(name = "elders") // <<--- 推荐添加，确保映射到正确的表名，通常是小写单数
public class Elder {
    @Id // <<--- 必须标注主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <<--- 标注主键生成策略，IDENTITY 表示数据库自增
    private Integer id; // 确保与数据库字段类型匹配
    private String name;
    private Integer age;
    private String birthDate; // 如果数据库是 DATE 类型，建议使用 java.time.LocalDate
    private String ethnicity;
    private String account;
    private String password; // ⚠️ 安全提醒：密码不应明文存储，通常应使用加密
    private String education;
    private String maritalStatus;
    private String hobbies;
    private String careLevel;
    private String medicalCare;
    private String feeType;
    private BigDecimal expenses;
    private String relativeContact;
    private String bedNumber; // 如果 bedNumber 是外键或关联实体，需要用 @ManyToOne/@OneToOne 注解

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime checkOutDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;
}