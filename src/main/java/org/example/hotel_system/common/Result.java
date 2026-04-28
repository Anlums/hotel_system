package org.example.hotel_system.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

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
