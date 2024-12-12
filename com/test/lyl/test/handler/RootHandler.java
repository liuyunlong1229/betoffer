package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public   class  RootHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response="hello world";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }



   boolean support(HttpExchange exchange){
        String requestUrl=exchange.getRequestURI().toString();
        return requestUrl.equals("/");

    }
}
