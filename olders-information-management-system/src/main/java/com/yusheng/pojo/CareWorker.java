package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareWorker {
    private Integer id;
    private String name;
    private Integer age;
    private String birthDate;
    private String ethnicity;
    private String account;
    private String password;
    private String education;
    private String experience;
    private String specialties;
    private String assignedElders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
