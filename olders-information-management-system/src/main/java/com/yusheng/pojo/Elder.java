// 文件路径: src/main/java/com/yusheng/pojo/Elder.java
package com.yusheng.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "elders")
public class Elder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "elders_id")
    private Integer id; // ✅ 匹配 `id` int NOT NULL AUTO_INCREMENT

    private String name;
    private Integer age;

    @Column(name = "birth_date")
    private LocalDate birthDate; // 匹配 `birth_date` date

    private String ethnicity;
    private String account;
    private String password;
    private String education;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(columnDefinition = "TEXT")
    private String hobbies;

    @Column(name = "care_level")
    private String careLevel;

    @Column(name = "medical_care", columnDefinition = "TEXT")
    private String medicalCare;

    @Column(name = "fee_type")
    private String feeType;

    private BigDecimal expenses;

    @Column(name = "relative_contact")
    private String relativeContact;

    @Column(name = "bed_number")
    private String bedNumber; // 保持为字符串，与数据库一致

    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}