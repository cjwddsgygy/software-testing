// 文件路径: src/main/java/com/yusheng/pojo/Expense.java
package com.yusheng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate; // 关键修改 #1: 引入 LocalDate
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("elderName")
    @Column(name = "elder")
    private String elder;

    @Column(name = "item_name")
    @JsonProperty("item")
    private String itemName;

    private BigDecimal amount;

    private String status;

    @JsonProperty("notes")
    @Column(name = "remark")
    private String remark;

    // 关键修改 #2: 将类型从 LocalDateTime 改为 LocalDate
    @Column(name = "expense_date")
    @JsonProperty("date")
    private LocalDate expenseDate;

    @Column(name = "created_at", updatable = false)
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}