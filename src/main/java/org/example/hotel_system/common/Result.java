package org.example.hotel_system.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 200成功, 400业务错误, 500系统崩溃
    private String msg;   // 给前端看的提示文字
    private T data;       // 数据

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setMsg("success");
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

}
