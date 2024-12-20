package com.test.lyl.test.exception;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.util.ResponseUtil;

public class ExceptionHandler {



    public void handle(HttpExchange exchange, Throwable ex){
        //打印错误日志，依赖具体的日志框架
        if(ex instanceof ParamException){ //请求参数错误
            ParamException pe=(ParamException)ex;
            ResponseUtil.writeResponse(exchange,400,"client request param error:"+pe.getMessage());
            return;
        }
        //服务器内部未捕获的异常
        ResponseUtil.writeResponse(exchange,500,"server inner error");
    }



}
