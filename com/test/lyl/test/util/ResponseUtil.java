package com.test.lyl.test.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {


   public static void writeResponse(HttpExchange exchange,int httpCode, String response) {
       try {
           exchange.sendResponseHeaders(httpCode, response.length());
           try (OutputStream os = exchange.getResponseBody()) {
               os.write(response.getBytes(StandardCharsets.UTF_8));
           }
       } catch (Exception e) {
           // 在这里可以添加异常处理逻辑，比如记录日志等
           e.printStackTrace();
       }
   }


    public static void writeSuccessResponse(HttpExchange exchange ,String response) {
        writeResponse(exchange,200,response);
    }
}
