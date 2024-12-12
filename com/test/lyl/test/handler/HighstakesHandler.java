package com.test.lyl.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.test.lyl.test.store.BetofferManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighstakesHandler extends RootHandler {

    @Override
    boolean support(HttpExchange exchange) {
        Pattern pattern = Pattern.compile("/(\\d+)/highstakes");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        return matcher.find();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Pattern pattern = Pattern.compile("/(\\d+)/highstakes");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().toString());
        matcher.find();
        int betofferid = Integer.parseInt(matcher.group(1));

        LinkedHashMap<Integer,Integer> resultMap = BetofferManager.getTop20List(betofferid);
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
        exchange.sendResponseHeaders(200, resp.length());
        OutputStream os = exchange.getResponseBody();
        os.write(resp.getBytes(StandardCharsets.UTF_8));
        os.close();



    }
}
