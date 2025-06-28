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

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // <<--- 必须添加这个注解
@Table(name = "beds") // <<--- 映射到数据库中的 'bed' 表
public class Bed {
    @Id // <<--- 必须标注主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <<--- 标注主键生成策略
    private Integer id;
    @Column(name = "room_number") // 驼峰命名转换为下划线命名
    private String roomNumber;
    @Column(name = "bed_number") // 驼峰命名转换为下划线命名
    private String bedNumber;
    private String status;
    @Column(name = "elders_id") // 明确映射到数据库的 elders_id 字段
    private Integer eldersId; // 对应数据库的 elders_id 字段
    @Column(name = "name")
    private Integer name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}