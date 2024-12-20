package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.test.lyl.test.util.ResponseUtil;

import java.io.IOException;

public   class  RootHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ResponseUtil.writeResponse(exchange,200,"hello world");
    }



   boolean support(HttpExchange exchange){
        String requestUrl=exchange.getRequestURI().toString();
        return requestUrl.equals("/");

    }
}
