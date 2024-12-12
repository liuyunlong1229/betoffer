package com.test.lyl.test.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {



    public static Map<String,String> getRequestParams(URI uri){



        String query = uri.getQuery();
        Map<String, String> parameters = new HashMap<>();
        if (query!= null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return parameters;
    }


    public  static String getRequestBody(InputStream inputStream){


        StringBuilder requestBodyContent = new StringBuilder();
       try {

           // 使用缓冲读取请求体
           InputStream requestBody = new BufferedInputStream(inputStream);
           byte[] buffer = new byte[1024];
           int bytesRead;

           while ((bytesRead = requestBody.read(buffer)) != -1) {
               requestBodyContent.append(new String(buffer, 0, bytesRead));
           }
       }catch (Exception ex){
           throw  new RuntimeException("read request body error",ex);
       }
       return requestBodyContent.toString();
    }


}
