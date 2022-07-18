package com.xiaoyaowind.common.controller;

import com.xiaoyaowind.common.exception.CustomizeException;
import com.xiaoyaowind.common.model.ResultCode;
import com.xiaoyaowind.common.model.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局错误处理
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({BindException.class})
    public Result MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new Result(ResultCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    @ExceptionHandler(CustomizeException.class)
    public Result CustomizeExceptionHandler(CustomizeException e) {
        // log.error(e.getMessage(), e); 由于还没集成日志框架，暂且放着，写上TODO
        return new Result(e.getCode(), e.getMsg(), e.getMessage());
    }
}
