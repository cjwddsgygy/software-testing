package com.yusheng.pojo;

import com.fasterxml.jackson.annotation.JsonFormat; // <<--- 导入这个注解
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate; // 我们依然建议将 checkInDate 改为 LocalDate
import java.time.LocalDateTime;

@Data
public class Elder {
    private Integer id;
    private String name;
    private Integer age;
    private String birthDate;
    private String ethnicity;
    private String account;
    private String password;
    private String education;
    private String maritalStatus;
    private String hobbies;
    private String careLevel;
    private String medicalCare;
    private String feeType;
    private BigDecimal expenses;
    private String relativeContact;
    private String bedNumber;

    // --- 核心修改在这里 ---

    // 对于需要精确时间的字段，使用 LocalDateTime 并加上注解
    // 这个注解会告诉 Jackson 如何将后端的数据格式化成 "yyyy-MM-dd HH:mm:ss" 字符串返回给前端
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime checkOutDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;
}