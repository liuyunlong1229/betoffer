package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.test.lyl.test.exception.ExceptionHandler;
import com.test.lyl.test.util.ResponseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChainHandler implements HttpHandler {

    List<RootHandler> handlerList=new ArrayList<>();


    ExceptionHandler exceptionHandler=new ExceptionHandler();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        for(RootHandler handler:handlerList){

           if(handler.support(exchange)){

               try {
                   handler.handle(exchange);
               }catch (Exception ex){
                   exceptionHandler.handle(exchange,ex);
               }
               return;
           }
        }

        ResponseUtil.writeResponse(exchange,404,"no handler for curr request");

    }


    public void addHandler(RootHandler handler){
        this.handlerList.add(handler);
    }

}
