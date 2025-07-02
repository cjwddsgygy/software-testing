// 文件路径: src/main/java/com/yusheng/pojo/CareWorker.java
package com.yusheng.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "care_workers")
public class CareWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 匹配 `id` int NOT NULL AUTO_INCREMENT

    private String name;
    private Integer age;

    @Column(name = "birth_date")
    private LocalDate birthDate; // 匹配 `birth_date` date

    private String ethnicity;
    private String account;
    private String password;
    private String education;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String specialties;

    @Column(name = "assigned_elders", columnDefinition = "TEXT")
    private String assignedElders;

    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}