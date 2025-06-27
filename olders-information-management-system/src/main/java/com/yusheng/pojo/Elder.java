package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Elder {
    private Integer eldersid;
    private String name;
    private Integer age;
    private String birthDate; // 可改为 LocalDate
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
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}