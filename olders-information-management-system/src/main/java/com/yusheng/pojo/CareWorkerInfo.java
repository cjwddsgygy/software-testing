package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装护工登录结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareWorkerInfo {
    private Integer id;
    private String name;
    private String account;
    private String token;
}
