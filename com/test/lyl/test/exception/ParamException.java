package com.test.lyl.test.exception;

/**
 * 客户端输入参数错误，抛出此异常，由异常处理器统一处理
 */
public class ParamException extends RuntimeException{


    public ParamException(String msg) {
        super(msg);
    }
}
