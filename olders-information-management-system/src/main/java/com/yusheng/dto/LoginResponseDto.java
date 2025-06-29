// 文件路径: src/main/java/com/yusheng/dto/LoginResponseDto.java (新建文件)
package com.yusheng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String role;
    private Object user; // 使用 Object 类型，可以灵活地存放 Admin, CareWorker 或 Elder 对象
}