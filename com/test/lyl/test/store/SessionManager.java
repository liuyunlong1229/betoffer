package com.test.lyl.test.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {


    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    //用于存储 userid 和 sessionkey 的映射关系
    static Map<String, Integer> sesionMap = new HashMap<>();

    // 用于存储 sessionkey 和 userid 的映射关系，方便反向查找
    static Map<Integer, String> userMap = new HashMap<>();


    static {
        // 启动定时任务，每1分钟检查一次是否有过期的 sessionkey
        executorService.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
           Iterator<Map.Entry<String,Integer>> it=sesionMap.entrySet().iterator();
           while (it.hasNext()){
               Map.Entry<String,Integer> entry= it.next();
               String sessionKey = entry.getKey();
               Integer userid= entry.getValue();

               long timestamp = Long.parseLong(sessionKey);
               if(currentTime - timestamp > 10 * 60 * 1000){ //10分钟session过期
                   it.remove();
                   userMap.remove(userid);
               }
           }
        }, 0, 1, TimeUnit.MINUTES);


    }


    /**
     * 根据用户id，生成sessionkey，简单实现，暂时用时间戳代替，可以保证不重复
     * @param userid
     * @return
     */
    public static String generateSessionKey(Integer userid) {
        String sessionKey=userMap.get(userid);
        if(sessionKey !=null){
            long timestamp = Long.parseLong(sessionKey);
            long currentTime = System.currentTimeMillis();

            if(currentTime - timestamp < 10 * 60 * 1000){
               return sessionKey;
            }

        }
        long timestamp = System.currentTimeMillis();
        sessionKey = timestamp+"";
        userMap.put(userid, sessionKey);
        sesionMap.put(sessionKey, userid);
        return sessionKey;
    }


    /**
     * 根据sesionkey，查找userid
     * @param sessionKey
     * @return
     */
    public static Integer getUserIdBySessionKey(String sessionKey) {
        long currentTime = System.currentTimeMillis();
        long timestamp = Long.parseLong(sessionKey);
        if(currentTime - timestamp > 10 * 60 * 1000){
            Integer userId=sesionMap.remove(sessionKey);
            userMap.remove(userId);
        }

        return sesionMap.get(sessionKey);
    }

}