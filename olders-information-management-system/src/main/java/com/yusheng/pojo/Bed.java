package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {
    private Integer id;
    private String roomNumber;
    private String bedNumber;
    private String status; // "空闲", "占用"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
