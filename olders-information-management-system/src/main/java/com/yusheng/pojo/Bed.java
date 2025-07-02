// 文件路径: src/main/java/com/yusheng/pojo/Bed.java
package com.yusheng.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "beds")
public class Bed {

    @Id                 // ✅ 关键点3: 必须有此注解，标注主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 关键点4: 标注主键自增策略
    private Integer id;

    // ✅ 关键点5: 使用 @Column 注解将Java的驼峰命名映射到数据库的下划线命名
    @Column(name = "elders_id")
    private Integer eldersId;

    // ✅ 关键点6: 修复了name的类型，从Integer改为String
    @Column(name = "name")
    private String name;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "bed_number")
    private String bedNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false) // 创建时自动填充，禁止更新
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}