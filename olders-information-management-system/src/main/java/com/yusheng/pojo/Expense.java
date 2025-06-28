package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity; // 新增导入
import jakarta.persistence.Id; // 新增导入
import jakarta.persistence.GeneratedValue; // 新增导入
import jakarta.persistence.GenerationType; // 新增导入
import jakarta.persistence.Table; // 新增导入
import jakarta.persistence.Column; // 新增导入

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // <<--- 必须添加这个注解
@Table(name = "expenses") // <<--- 映射到数据库中的 'expenses' 表 (根据您提供的 SQL，表名是 expenses)
public class Expense {
    @Id // <<--- 必须标注主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <<--- 标注主键生成策略
    private Integer id;
    private String elder; // 通常这里会是 elderId，然后通过关联获取 elderName
    @Column(name = "item_name") // 驼峰命名转换为下划线命名
    private String itemName;
    private BigDecimal amount;
    private String remark;
    @Column(name = "expense_date") // 驼峰命名转换为下划线命名
    private LocalDateTime expenseDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
