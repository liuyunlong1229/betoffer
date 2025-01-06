package com.test.lyl.test.store;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BetofferManager {



    //保留每个投注-个用户-的报价
    static Map<Integer, ConcurrentHashMap<Integer,Integer>>  betofferMap=new ConcurrentHashMap<>();

    /**
     * 提交报价
     * @param betofferId 投注
     * @param userId 用户id
     * @param stake 报价
     * @return 1- 新的报价设置成果 0-没有设置成果
     */
    public  static Integer putStake(Integer betofferId,Integer userId,Integer stake) {

        ConcurrentHashMap<Integer, Integer> subMap = betofferMap.get(betofferId);

        if (subMap != null) {
            Integer oldStake = subMap.get(userId);
            if (oldStake ==null) {  //该用户没有报价
                subMap.put(userId, stake);
                resizeCapacitylimit(subMap,betofferId);  //有新的报价进来，需要判断是否超过20个，超过进行剔除
            }else{
                if(oldStake<stake){ //替换成最高的报价，同一个用户，此次不需要重新排序，在查询的时候会排
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
    public static void resizeCapacitylimit(ConcurrentHashMap<Integer, Integer> subMap,Integer betofferId ) {


        //针对某个投注报价多余20个
        if (subMap.size() > 20) {

            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(subMap.entrySet());

            // 使用Stream API进行排序，按照value降序排列
            entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // 保留value最大的20个元素，如果总数不足20个，则取全部元素
            int size = Math.min(20, entryList.size());
            List<Map.Entry<Integer, Integer>> top20List = entryList.subList(0, size);

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

