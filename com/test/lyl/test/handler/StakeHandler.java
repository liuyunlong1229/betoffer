package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.exception.ParamException;
import com.test.lyl.test.store.BetofferManager;
import com.test.lyl.test.store.SessionManager;
import com.test.lyl.test.util.RequestUtil;
import com.test.lyl.test.util.ResponseUtil;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StakeHandler extends RootHandler {


    String patternStr = "^\\/([^/]+)\\/stake$";
    Pattern pattern = Pattern.compile(patternStr);



    @Override
    boolean support(HttpExchange exchange) {

        String requestMethod=exchange.getRequestMethod();
        if(!requestMethod.equalsIgnoreCase("post")){
            return false;
        }

        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        return matcher.find();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        URI uri= exchange.getRequestURI();
        Matcher matcher = pattern.matcher(uri.toString());
        matcher.find();


        int betofferId;
        try {
            betofferId = Integer.parseInt(matcher.group(1));
        }catch (Exception ex){
            throw new ParamException(" The betofferId  must be a number");
        }

        Map<String,String> queryParams= RequestUtil.getRequestParams(uri);
        String sessionKey= queryParams.get("sessionkey");
        Integer userId=SessionManager.getUserIdBySessionKey(sessionKey);
        if(userId==null){
            String resp="sessionKey has expired or sessionKey not exists";
            ResponseUtil.writeResponse(exchange,401,resp);
            return ;

        }
        String stakeStr=RequestUtil.getRequestBody(exchange.getRequestBody());
        Integer stake= Integer.parseInt(stakeStr);

        BetofferManager.putStake(betofferId,userId,stake);
        ResponseUtil.writeSuccessResponse(exchange,"");

    }



}
