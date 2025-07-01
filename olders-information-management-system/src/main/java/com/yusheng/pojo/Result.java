package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一的 API 响应结果封装类
 * @param <T> 响应数据的类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    /**
     * 业务状态码 (0-成功, 其他-失败)
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    // --- 成功时的静态工厂方法 ---

    /**
     * 返回带数据的成功响应
     * @param data 响应数据
     * @return 包含数据的 Result 对象
     * @param <E> 数据的泛型类型
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 返回不带数据的成功响应（常用于删除、修改等操作）
     * @return 不包含数据的 Result 对象
     */
    public static <E> Result<E> success() {
        return new Result<>(0, "操作成功", null);
    }

    // --- 失败时的静态工厂方法 ---

    /**
     * 返回带错误消息的失败响应 (使用默认错误码 1)
     * @param msg 错误消息
     * @return 包含错误信息的 Result 对象
     */
    public static <E> Result<E> error(String msg) {
        return new Result<>(1, msg, null);
    }

    /**
     * 返回可自定义错误码和错误消息的失败响应
     * @param code 自定义错误码
     * @param msg  错误消息
     * @return 包含错误信息的 Result 对象
     */
    public static <E> Result<E> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }
}