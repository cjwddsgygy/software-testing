// 文件路径: src/main/java/com/yusheng/pojo/HealthRecord.java
package com.yusheng.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "health_records")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ 匹配 `elder_name` varchar(255) NOT NULL
    @Column(name = "elder_name", nullable = false)
    private String elderName;

    // ✅ 匹配 `record_type` varchar(255) NOT NULL
    @Column(name = "record_type", nullable = false)
    private String recordType;

    // ✅ 匹配 `value` varchar(255) NOT NULL
    @Column(name = "value", nullable = false)
    private String value;

    // ✅ 匹配 `record_date` date NOT NULL
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    // ✅ 匹配 `careworker_name` varchar(255) NULL
    @Column(name = "careworker_name")
    private String careworkerName;

    // ✅ 匹配 `notes` varchar(500) NULL
    @Column(name = "notes", length = 500)
    private String notes;


    // ✅ 匹配 `description` text
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // ✅ 匹配 `doctor_notes` text NULL
    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;

    // ✅ 匹配 `elder` varchar(255)
    @Column(name = "elder")
    private String elder;

    // ✅ 匹配时间戳字段
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}