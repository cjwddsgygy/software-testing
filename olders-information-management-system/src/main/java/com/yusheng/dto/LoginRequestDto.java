// 文件路径: src/main/java/com/yusheng/dto/LoginRequestDto.java
package com.yusheng.dto;

import jakarta.validation.constraints.NotBlank; // ✅ 导入校验注解
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "登录账号不能为空") // ✅ 校验：不能为空或仅包含空白字符
    private String account;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    @NotBlank(message = "登录角色不能为空")
    private String role;
}