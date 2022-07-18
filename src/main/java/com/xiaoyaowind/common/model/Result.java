package com.xiaoyaowind.common.model;

import com.xiaoyaowind.common.interfaces.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    // 状态码
    private int code;

    // 状态信息
    private String msg;

    // 返回对象
    private T data;


    // 只返回状态码
    public Result(ExceptionCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = null;
    }

    public Result(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
        this.data = data;
    }

    public Result(ResultCode resultCode, String defaultMessage) {
        this.code = resultCode.getCode();
        this.msg = defaultMessage;
        this.data = null;
    }


    public static <T> Result<T> fail(Integer code, String message) {
        return Result.getResult(code, null, message);
    }

    private static <T> Result<T> success(ResultCode resultCode, T data) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        result.setData(data);
        return result;
    }


    private static <T> Result<T> getResult(Integer code, T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        result.setData(data);
        return result;
    }
}
