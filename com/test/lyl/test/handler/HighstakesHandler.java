package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.exception.ParamException;
import com.test.lyl.test.store.BetofferManager;
import com.test.lyl.test.util.ResponseUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighstakesHandler extends RootHandler {

   //Pattern pattern = Pattern.compile("/(\\d+)/highstakes");


    String patternStr = "^\\/([^/]+)\\/highstakes$";
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
        //由于support方法中已经进行匹配，所以可以之际从matcher.group(1)中取值

        int betofferId;
        try {
            betofferId = Integer.parseInt(matcher.group(1));
        }catch (Exception ex){
            throw new ParamException("The betofferId must be a number");
        }


        LinkedHashMap<Integer,Integer> resultMap = BetofferManager.getTop20List(betofferId);
        StringBuilder response=new StringBuilder();
        int index=0;
        if(resultMap !=null){
           Iterator<Map.Entry<Integer,Integer>> it=resultMap.entrySet().iterator();
           while (it.hasNext()){
               Map.Entry<Integer,Integer> entry= it.next();
               Integer userid=entry.getKey();
               Integer stake=entry.getValue();
               if(index !=0){
                   response.append(",");
               }
               response.append(userid).append("=").append(stake);
               index++;
           }

        }
        String resp=response.toString();
        ResponseUtil.writeSuccessResponse(exchange,resp);



    }
}
