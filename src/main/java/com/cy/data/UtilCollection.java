package com.cy.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;

/**
 * 1、object compare Java多属性对象比较器
 */
public class UtilCollection {

    public static <E> boolean notEmpty(List<E> list) {
        return list != null && list.size() > 0;
    }

    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.size() == 0;
    }

    public static <K,V> boolean notEmpty(Map<K,V> map) {
        return map != null && map.size() > 0;
    }

    public static <K,V> boolean isEmpty(Map<K,V> map) {
        return map == null || map.size() == 0;
    }

    /**for short:
     * mPushMsgListVos.addAll( Arrays.asList(pushMsgListVos));
     * @param array
     * @param <E>
     * @return
     */
    public static <E> List<E> convert_arrayToList(E[] array) {

        List<E> userList = new ArrayList<E>();

        if (array == null || array.length == 0) {
            return userList;
        }

        addAll(userList, array);
        return userList;
    }

    /**会出错
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E[] convert_listToArray(List<E> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        E[] array = (E[]) list.toArray();
        return array;
    }

    /**
     * @param list  really arraylist
     * @param contents  eg:  new String[receivers.size()]
     * @param <E>
     * @return
     */
    public static <E> E[] listToArray(List<E> list,E[] contents){
        if (list==null || list.size()==0){
            return null;
        }
        ArrayList<E> list1= (ArrayList<E>) list;
        return list1.toArray(contents);
    }

    /**list去重
     * @param list
     * @param <E>
     * @return
     */
    public static <E> List<E> singleFilter(List<E> list){
        ArrayList<E> result = new ArrayList<E>();

        for(E e: list){
            if(Collections.frequency(result, e) < 1) result.add(e);
        }
        return result;
    }

    public static <T> Set convert(List<T> list){
        if (isEmpty(list)) return null;

        HashSet hashSet=new HashSet<T>();
        hashSet.addAll(list);
        return hashSet;
    }

    public static <T> ArrayList<T> convert(Set<T> set){
        return new ArrayList(set);
    }

    /**
     * 排列组合
     * {我，你，他} {zhu，mao，gou} {1，2}
     * ————————→
     * {我,zhu,1} {我,zhu,2} {我,mao,1} {我,mao,2} {我,gou,1} {我,gou,2} {你,zhu,1} ...
     * @param lists
     * @return
     */
    protected static <T> List<List<T>> combination(List<List<T>> lists) {
        List<List<T>> resultLists = new ArrayList<List<T>>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<T>());
            return resultLists;
        } else {
            List<T> firstList = lists.get(0);
            List<List<T>> remainingLists = combination(lists.subList(1, lists.size()));
            for (T condition : firstList) {
                for (List<T> remainingList : remainingLists) {
                    ArrayList<T> resultList = new ArrayList<T>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }

    /**列表内容相同性比较
     * @param list1
     * @param list2
     * @return
     */
    public static boolean compare(List<String> list1, List<String> list2) {
        Set<String> aSet = new HashSet<String>();
        Set<String> bSet = new HashSet<String>();
        aSet.addAll(list1);
        bSet.addAll(list2);
        if (aSet.size() != bSet.size()) {
            return false;
        } else {
            int tempASetSize = aSet.size();
            aSet.addAll(list2);
            return tempASetSize == aSet.size();
        }
    }

    public static <E> String toString(List<E> list){
        return list.toString().replaceAll(" ","");
    }

    public static void putAll(Map map,Map mapToBeAdded){
        if (!isEmpty(mapToBeAdded)){
            map.putAll(mapToBeAdded);
        }
    }

    public static <K,V> String toString(Map<K,V> map){
        StringBuilder sb=new StringBuilder();
        for (Map.Entry<K,V> entry : map.entrySet()) {
            sb.append("key:"+entry.getKey()+" value:"+entry.getValue());
        }
        return sb.toString();
    }
}