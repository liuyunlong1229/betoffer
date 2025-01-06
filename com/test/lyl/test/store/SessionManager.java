package com.test.lyl.test.store;

import com.test.lyl.test.constant.ConstantVar;
import com.test.lyl.test.vo.Token;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SessionManager {


    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    // 用于存储 sessionkey 和 Token 的映射关系，方便反向查找
    static Map<String, Token> sesionMap = new ConcurrentHashMap<>();


    //用于存储 userId 和 Token 的映射关系
    static Map<Integer, Token> userMap = new ConcurrentHashMap<>();

    static {
        // 启动定时任务，每1分钟检查一次是否有过期的 sessionkey
        executorService.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
           Iterator<Map.Entry<String,Token>> it=sesionMap.entrySet().iterator();
           while (it.hasNext()){
               Map.Entry<String,Token> entry= it.next();
               Token token= entry.getValue();
               Integer userId=token.getUserId();
               if(token.isExpired()){ //10分钟session过期
                   it.remove();
                   userMap.remove(userId);
               }
           }
        }, 0, 1, TimeUnit.MINUTES);


    }


    /**
     * 根据用户id获取sessionKey简单实现
     * @param userId 用户id
     * @return sessionKey
     */
    public static String buildSessionKey(Integer userId) {

        Token token= userMap.get(userId);
        if(token !=null){
            Long timestamp=token.getPubTime();
            long currentTime = System.currentTimeMillis();

            if(currentTime - timestamp < ConstantVar.TOKEN_EXPIRED_TIME){
                return token.getSessionKey();
            }

        }

        long timestamp = System.currentTimeMillis();
        String sessionKey = buildSessionKey();
        token=new Token(userId,sessionKey,timestamp);
        userMap.put(userId, token);
        sesionMap.put(sessionKey, token);

        return sessionKey;
    }


    /**
     * 根据sesionkey，查找userid
     * @param sessionKey
     * @return
     */
    public static Integer getUserIdBySessionKey(String sessionKey) {
        long currentTime = System.currentTimeMillis();
        Token token = sesionMap.get(sessionKey);

        if (token != null) {
            Integer userId = token.getUserId();
            if (token.isExpired()) {
                sesionMap.remove(sessionKey);
                userMap.remove(userId);
            }

        }
        return null;
    }



    /**
     * 随机生成7位长度的sessionkey包含大写字母和数字,并判断是内容是否重复，需要保证唯一性
     * @return  sessionKey字符串
     */
    public static String buildSessionKey() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        int length=ConstantVar.RANDOM_STRING_FACTORY.length();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(length);
            result.append(ConstantVar.RANDOM_STRING_FACTORY.charAt(index));
        }

        String sessionKey=result.toString();
        if(sesionMap.containsKey(sessionKey)){
            return buildSessionKey();
        }
        return sessionKey;



   }

}