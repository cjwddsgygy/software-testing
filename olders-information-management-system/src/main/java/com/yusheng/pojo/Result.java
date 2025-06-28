package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code; // 0-成功, 1-失败
    private String msg;   // 提示信息
    private T data;      // 数据 data

    // --- 成功时的静态工厂方法 ---

    /**
     * 返回带数据的成功响应
     * @param data 响应数据
     * @return Result 对象
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 返回不带数据的成功响应
     * @return Result 对象
     */
    public static <E> Result<E> success() {
        return new Result<>(0, "操作成功", null);
    }

    // --- 失败时的静态工厂方法 ---

    /**
     * 返回带错误消息的失败响应 (使用默认错误码 1)
     * @param msg 错误消息
     * @return Result 对象
     */
    public static <E> Result<E> error(String msg) {
        return new Result<>(1, msg, null);
    }

    /**
     * ✅ 核心修改：新增一个可以自定义错误码和错误消息的失败响应方法
     * @param code 错误码 (Integer类型，与你的类定义保持一致)
     * @param msg  错误消息
     * @return Result 对象
     */
    public static <E> Result<E> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * ✅ 兼容修改：也增加一个接收String类型code的方法，避免潜在的类型问题
     * @param code 错误码 (String类型)
     * @param msg  错误消息
     * @return Result 对象
     */
    public static <E> Result<E> error(String code, String msg) {
        try {
            // 尝试将String类型的code转换为Integer
            return new Result<>(Integer.parseInt(code), msg, null);
        } catch (NumberFormatException e) {
            // 如果转换失败，使用默认错误码1
            return new Result<>(1, msg, null);
        }
    }
}