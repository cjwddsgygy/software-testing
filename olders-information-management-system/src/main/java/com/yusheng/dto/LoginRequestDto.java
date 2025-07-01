// 文件路径: src/main/java/com/yusheng/dto/LoginRequestDto.java

package com.yusheng.dto;

import lombok.Data;

/**
 * 统一登录请求的数据传输对象 (DTO)
 * 用于封装从前端发送过来的登录信息。
 */
@Data // Lombok 注解，自动生成 getter, setter, toString, equals, hashCode 等方法
public class LoginRequestDto {

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录角色 ('admin', 'careworker', 'elder')
     */
    private String role;
}