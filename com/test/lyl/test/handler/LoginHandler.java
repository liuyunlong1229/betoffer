package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.exception.ParamException;
import com.test.lyl.test.store.SessionManager;
import com.test.lyl.test.util.ResponseUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginHandler extends RootHandler {



    String patternStr = "^\\/([^/]+)\\/session$";
    Pattern pattern = Pattern.compile(patternStr);

    @Override
    boolean support(HttpExchange exchange) {
        String requestMethod=exchange.getRequestMethod();
        if(!requestMethod.equalsIgnoreCase("get")){
           return false;
        }
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        return matcher.find();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        matcher.find();

        int userId;
        try{
            userId =  Integer.parseInt(matcher.group(1));
        }catch (Exception ex){
            throw new  ParamException("The user ID must be a number");
        }

        String sessionKey=SessionManager.buildSessionKey(userId);
        ResponseUtil.writeSuccessResponse(exchange,sessionKey);


    }



}
