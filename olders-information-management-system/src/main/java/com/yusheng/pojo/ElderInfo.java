package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装老人登录结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderInfo {
    private Integer id;
    private String name;
    private String account;
    private String token;
}
