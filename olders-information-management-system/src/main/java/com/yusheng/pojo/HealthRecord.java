package com.yusheng.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // 声明这是一个 JPA 实体
@Table(name = "health_records") // 映射到数据库的 health_records 表
public class HealthRecord {

    @Id // 声明主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    private Integer id;

    @Column(name = "elder_name", nullable = false)
    private String elderName; // 老人姓名

    @Column(name = "record_type", nullable = false)
    private String recordType; // 记录类型

    @Column(nullable = false)
    private String value; // 测量值

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate; // 记录日期 (使用 LocalDate 类型更合适)

    @Column(name = "careworker_name", nullable = false)
    private String careworkerName; // 记录护工

    @Column(length = 500) // 可以指定列的长度
    private String notes; // 备注

    @CreationTimestamp // 由 Hibernate 自动在创建时填充
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // 由 Hibernate 自动在更新时填充
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
