package com.test.lyl.test.store;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BetofferManager {


    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    //保留每个投注-个用户-的报价k
   static Map<Integer, ConcurrentHashMap<Integer,Integer>>  betofferMap=new ConcurrentHashMap<>();


    /**
     * 定时清理每个投注里面的报价，超过20个保留报价高的20个
     */
    static {
        // 启动定时任务，每1分钟检查一次是否有过超过20个投票
        executorService.scheduleAtFixedRate(() -> {

            Iterator<Map.Entry<Integer, ConcurrentHashMap<Integer, Integer>>> it=betofferMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<Integer, ConcurrentHashMap<Integer, Integer>> entry= it.next();
                if(entry.getValue().size()>20){
                    reserve(entry.getValue(),entry.getKey());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);


    }

    /**
     * 提交报价
     * @param betofferId 投注
     * @param userId 用户id
     * @param stake 报价
     * @return 1- 新的报价设置成功 0-没有设置成功
     */
   public  static Integer putStake(Integer betofferId,Integer userId,Integer stake) {

       ConcurrentHashMap<Integer, Integer> subMap = betofferMap.get(betofferId);

       if (subMap != null) {
           Integer oldStake = subMap.get(userId);
           if (oldStake ==null) {
               subMap.put(userId, stake);
           }else{
               if(oldStake<stake){ //替换成最高的报价，同一个用户
                   subMap.put(userId,stake);
               }else{
                   return 0;
               }
           }
           return 1;


       } else {
           subMap = new ConcurrentHashMap<>();
           betofferMap.put(betofferId, subMap);
           subMap.put(userId, stake);
           return 1;
       }



   }


    /**
     * 按照报价排序，保留最大的20个报价
     * @param subMap  用户和报价映射
     * @param betofferId 投注id
     */
   public static void reserve(ConcurrentHashMap<Integer, Integer> subMap,Integer betofferId ) {


       //针对某个投注报价多余20个
       if (subMap.size() > 20) {

           List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(subMap.entrySet());

           // 使用Stream API进行排序，按照value降序排列
           entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

           List<Map.Entry<Integer, Integer>> top20List = entryList.subList(0, 20);

           subMap = null;

           ConcurrentHashMap newMap= top20List.stream().collect(Collectors.toMap(
                   Map.Entry::getKey,
                   Map.Entry::getValue,
                   (oldValue, newValue) -> oldValue,
                   ConcurrentHashMap::new
           ));

           betofferMap.put(betofferId, newMap);

       }
   }


    /**
     * 根据投注id，获取最大的20个报价列表
     * @param betofferId 投注id
     * @return 报价列表
     */
   public static LinkedHashMap<Integer,Integer> getTop20List(Integer betofferId) {
       ConcurrentHashMap<Integer, Integer> concurrentHashMap = betofferMap.get(betofferId);
       if (concurrentHashMap != null && concurrentHashMap.size() > 1) {
           List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(concurrentHashMap.entrySet());
           // 使用Stream API进行排序，按照value降序排列
           entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
           List<Map.Entry<Integer, Integer>> top20List =entryList.size()>20? entryList.subList(0, 20):entryList;
           //构建linkedlistmap按照顺序保存
           return top20List.stream().collect(Collectors.toMap(
                   Map.Entry::getKey,
                   Map.Entry::getValue,
                   (oldValue, newValue) -> oldValue,
                   LinkedHashMap::new
           ));

       }

       return null;
   }

}
