package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.store.BetofferManager;
import com.test.lyl.test.store.SessionManager;
import com.test.lyl.test.util.RequestUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StakeHandler extends RootHandler {


    @Override
    boolean support(HttpExchange exchange) {
        Pattern pattern = Pattern.compile("/(\\d+)/stake");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        return matcher.find();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Pattern pattern = Pattern.compile("/(\\d+)/stake");
        URI uri= exchange.getRequestURI();
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        matcher.find();
        int betofferId = Integer.parseInt(matcher.group(1));
        Map<String,String> queryParams= RequestUtil.getRequestParams(uri);
        String sessionKey= queryParams.get("sessionkey");
        Integer userId=SessionManager.getUserIdBySessionKey(sessionKey);
        if(userId==null){
            String resp="sessionKey has expired,or sessionKey not exists";
            exchange.sendResponseHeaders(401, resp.length());
            OutputStream os = exchange.getResponseBody();
            os.write(resp.getBytes(StandardCharsets.UTF_8));
            os.close();

        }
        String stakeStr=RequestUtil.getRequestBody(exchange.getRequestBody());
        Integer stake= Integer.parseInt(stakeStr);

        BetofferManager.putStake(betofferId,userId,stake);

        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write("".getBytes(StandardCharsets.UTF_8));
        os.close();


    }



}
