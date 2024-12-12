package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.store.SessionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginHandler extends RootHandler {


    @Override
    boolean support(HttpExchange exchange) {
        Pattern pattern = Pattern.compile("/(\\d+)/session");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        return matcher.find();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Pattern pattern = Pattern.compile("/(\\d+)/session");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        matcher.find();
        int userId = Integer.parseInt(matcher.group(1));
        String sessionKey=SessionManager.generateSessionKey(userId);
        exchange.sendResponseHeaders(200, sessionKey.length());
        OutputStream os = exchange.getResponseBody();
        os.write(sessionKey.getBytes(StandardCharsets.UTF_8));
        os.close();


    }



}
