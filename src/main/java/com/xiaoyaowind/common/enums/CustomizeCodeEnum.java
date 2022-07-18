package com.xiaoyaowind.common.enums;

import com.xiaoyaowind.common.interfaces.ExceptionCode;
import lombok.Getter;

@Getter
public enum CustomizeCodeEnum implements ExceptionCode {

    APP_ERROR(2000, "业务异常"),;

    private int code;
    private String msg;

    CustomizeCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
