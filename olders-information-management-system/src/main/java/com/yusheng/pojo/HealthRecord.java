package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {
    private Integer id;
    private String elder;
    private String recordDate; // 可改为 LocalDate
    private String description;
    private String doctorNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
