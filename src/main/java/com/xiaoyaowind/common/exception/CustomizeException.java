package com.xiaoyaowind.common.exception;

import com.xiaoyaowind.common.interfaces.ExceptionCode;
import com.xiaoyaowind.common.enums.CustomizeCodeEnum;
import lombok.Getter;

@Getter
public class CustomizeException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public CustomizeException(ExceptionCode exceptionCode, String message) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(message);
        // 状态码
        this.code = exceptionCode.getCode();
        // 状态码配套的msg
        this.msg = exceptionCode.getMsg();
    }

    // 默认异常使用APP_ERROR状态码
    public CustomizeException(String message) {
        super(message);
        this.code = CustomizeCodeEnum.APP_ERROR.getCode();
        this.msg = CustomizeCodeEnum.APP_ERROR.getMsg();
    }

}
