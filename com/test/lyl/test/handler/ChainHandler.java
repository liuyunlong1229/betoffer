package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChainHandler implements HttpHandler {

    List<RootHandler> handlerList=new ArrayList<>();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        for(RootHandler handler:handlerList){

           if(handler.support(exchange)){
               handler.handle(exchange);
               return;
           }
        }

        String resp="no handler for curr request";
        exchange.sendResponseHeaders(404, resp.length());
        OutputStream os = exchange.getResponseBody();
        os.write(resp.getBytes(StandardCharsets.UTF_8));
        os.close();

    }


    public void addHandler(RootHandler handler){
        this.handlerList.add(handler);
    }

}
