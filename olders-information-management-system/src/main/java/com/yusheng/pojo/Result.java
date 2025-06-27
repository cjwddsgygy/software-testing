package com.yusheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
// 1. 在类名后面加上 <T>，声明这是一个泛型类
public class Result<T> {
    private Integer code; // 0-成功, 1-失败
    private String msg;   // 提示信息

    // 2. 将 data 字段的类型从 Object 改为 T
    private T data;      // 数据 data

    // 3. 修改 success 静态方法，使其也成为泛型方法，并能正确构造 Result<T>
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    // 如果需要一个不带数据的成功返回，可以像这样重载
    public static <E> Result<E> success() {
        return new Result<>(0, "操作成功", null);
    }

    // 4. 修改 error 静态方法，使其返回一个指定类型的空数据 Result
    public static <E> Result<E> error(String msg) {
        return new Result<>(1, msg, null);
    }
}
