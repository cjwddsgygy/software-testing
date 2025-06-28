// backend/src/main/java/com/yusheng/common/Result.java
package com.yusheng.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用API响应结果类
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code; // 业务状态码，0表示成功，其他表示失败
    private String msg; // 响应消息
    private T data; // 响应数据

    // 成功响应，带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }

    // 成功响应，不带数据
    public static Result<Void> success() {
        return new Result<>(0, "操作成功", null);
    }

    // 失败响应，带消息
    public static Result<Void> fail(String msg) {
        return new Result<>(1, msg, null); // 默认错误码1
    }

    // 失败响应，带错误码和消息
    public static Result<Void> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 失败响应，从异常中获取消息
    public static Result<Void> fail(Exception e) {
        return new Result<>(1, e.getMessage(), null);
    }
}